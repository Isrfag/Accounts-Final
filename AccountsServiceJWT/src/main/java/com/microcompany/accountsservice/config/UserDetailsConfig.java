package com.microcompany.accountsservice.config;
import com.microcompany.accountsservice.model.ERole;
import com.microcompany.accountsservice.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDetailsConfig {


    @Bean
    public UserDetailsService userDetailsService() {

        return new UserDetailsService() {
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

                return User.builder()
                        .id(1)
                        .email(email)
                        .role(ERole.DIRECTOR)
                        .password("password")
                        .build();

            }
        };
    }
}
