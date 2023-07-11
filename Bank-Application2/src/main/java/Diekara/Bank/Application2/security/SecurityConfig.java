package Diekara.Bank.Application2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @EnableMethodSecurity
    @EnableWebSecurity
    public class SecurityConfig {

        //Password encoder
        //Setup users
        //add security filter

        private UserDetailsService userDetailsService;

        public SecurityConfig (UserDetailsService userDetailsService){
            this.userDetailsService=userDetailsService;
        }

        @Bean
        public static PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
        }
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorize ->
                            //authorize.anyRequest().authenticated())
                            authorize.requestMatchers(HttpMethod.GET,"/api/bank/**").permitAll()
                                    .requestMatchers("/api/auth/**").permitAll()
//                                    .requestMatchers("/api/auth/signin").authenticated()
//                                    .requestMatchers("/api/bank/credit").hasRole("USER")
                                    .anyRequest()
                                    .authenticated()
                    )
                    .httpBasic(Customizer.withDefaults());


            return httpSecurity.build();


        }
//        @Bean
//        public UserDetailsService userDetailsService(){
//             UserDetails firstUser = User.builder()
//                     .username("Serah")
//                     .password(passwordEncoder().encode("iacademy"))
//                     .roles("USER")
//                     .build();
//
//             UserDetails secondUser =User.builder()
//                     .username("Tosin")
//                     .password(passwordEncoder().encode("tosin"))
//                     .roles("ADMIN")
//                     .build();
//
//             return new InMemoryUserDetailsManager(firstUser, secondUser);

    }
//    }





