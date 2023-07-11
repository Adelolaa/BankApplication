package Diekara.Bank.Application2.service;

import Diekara.Bank.Application2.Repository.UserRepository;
import Diekara.Bank.Application2.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


    @Service
    public class CustomerDetailsService implements UserDetailsService {
        private UserRepository userRepository;

        public CustomerDetailsService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
            User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User with provided credentials not found! "
                            + usernameOrEmail));

            //retrieve roles associated with the user
            Set<GrantedAuthority> authorities = user.getRoles().stream()
                    .map((role) -> new SimpleGrantedAuthority(role.getRoleName()))
                    .collect(Collectors.toSet());

            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(), authorities);





            /**
             * logic to load user by username or email from db
             */
        }
    }


