package com.microcompany.accountsservice.config;
import com.microcompany.accountsservice.model.ERole;
import com.microcompany.accountsservice.model.User;
import com.microcompany.accountsservice.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsConfig {

     @Autowired
     private UserRepository userRepository;
    @Bean
    public UserDetailsService userDetailsService() {

        return new UserDetailsService() {
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

                return userRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new UsernameNotFoundException(
                                        "User " + email + " not found"
                                )
                        );

            }
        };
    }
}
