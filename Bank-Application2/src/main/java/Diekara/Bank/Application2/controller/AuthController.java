package Diekara.Bank.Application2.controller;

import Diekara.Bank.Application2.Repository.RoleRepository;
import Diekara.Bank.Application2.Repository.UserRepository;
import Diekara.Bank.Application2.dto.LoginDto;
import Diekara.Bank.Application2.dto.RegisterDto;
import Diekara.Bank.Application2.entity.Role;
import Diekara.Bank.Application2.entity.User;
import Diekara.Bank.Application2.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;


    @RestController
    @RequestMapping("/api/auth")

    public class AuthController {

        @Autowired
        private AuthenticationManager authenticationManager;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private RoleRepository roleRepository;
        //    Logger logger = (Logger) LoggerFactory.getLogger(Logger.class);
        @Autowired
        PasswordEncoder passwordEncoder;
        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto){
            if (userRepository.existsByUsername(registerDto.getUsername())){
                return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
            }

            User user = User.builder()
                    .firstName(registerDto.getFirstName())
                    .otherName(registerDto.getOtherName())
                    .lastName(registerDto.getLastName())
                    .username(registerDto.getUsername())
                    .email(registerDto.getEmail())
                    .password(passwordEncoder.encode(registerDto.getPassword()))
                    .accountNumber(ResponseUtil.generateAccountNumber(10))
                    .accountBalance(BigDecimal.ZERO)
                    .build();
            Role roles = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            user.setRoles(Collections.singleton(roles));

            userRepository.save(user);

            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        }
        @PostMapping("/signin")
        public ResponseEntity<String>  login(@RequestBody LoginDto loginDto){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>("User sign in successfully", HttpStatus.OK);
        }



    }


