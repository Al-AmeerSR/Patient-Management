// Marks this class as a source of bean definitions for the Spring container
package com.pm.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Indicates that this class contains configuration related to Spring Security
@Configuration
public class SecurityConfig {

    // This method defines the security filter chain for handling HTTP security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Configures the HTTP request authorization rules
        // .anyRequest().permitAll() means all requests are allowed without authentication
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())

                // Disables CSRF (Cross Site Request Forgery) protection
                // This is often disabled in stateless APIs or during development
                .csrf(AbstractHttpConfigurer::disable);

        // Builds and returns the configured SecurityFilterChain
        return http.build();
    }

    // Defines a bean for PasswordEncoder which will be used to encode and verify passwords
    @Bean
    public PasswordEncoder passwordEncoder() {

        // BCrypt is a strong hashing function designed for password storage
        return new BCryptPasswordEncoder();
    }
}
