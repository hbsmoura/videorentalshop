package com.hbsmoura.videorentalshop.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeHttpRequests()

                // Employee
                .requestMatchers(HttpMethod.POST, "/employees/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.GET, "/employees/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PATCH, "/employees/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT, "/employees/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.DELETE, "/employees/**").hasRole("MANAGER")

                // Client
                .requestMatchers(HttpMethod.POST, "/clients/**").anonymous()
                .requestMatchers(HttpMethod.GET, "/clients/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.PATCH, "/clients/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.PUT, "/clients/**").hasRole("CLIENT")
                .requestMatchers(HttpMethod.DELETE, "/clients/**").hasRole("MANAGER")

                // Movie
                .requestMatchers(HttpMethod.GET, "/movies/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/movies/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.PUT, "/movies/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.DELETE, "/movies/**").hasRole("MANAGER")

                //Booking
                .requestMatchers(HttpMethod.POST, "/bookings/**").hasRole("CLIENT")
                .requestMatchers(HttpMethod.GET, "/bookings/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.PATCH, "/bookings/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.PUT, "/bookings/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/bookings/**").hasRole("MANAGER")

                .anyRequest()
                .denyAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
