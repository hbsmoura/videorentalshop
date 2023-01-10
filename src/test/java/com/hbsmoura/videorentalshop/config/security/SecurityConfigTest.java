package com.hbsmoura.videorentalshop.config.security;


import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.model.Employee;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@TestConfiguration
@EnableMethodSecurity
public class SecurityConfigTest {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userService() {
        Employee employee = Employee.builder()
                .id(UUID.fromString("7b8e6d52-9534-45d6-a8f9-43c41082d7ff"))
                .name("Mocked Employee")
                .username("mockedemployee")
                .password(passwordEncoder().encode("pass"))
                .manager(false)
                .build();

        Client client = Client.builder()
                .id(UUID.fromString("40036fd4-9090-480b-8125-142a85794d4f"))
                .name("Mocked Client")
                .username("mockedclient")
                .password(passwordEncoder().encode("pass"))
                .build();

        return new CustomUserDetailsServiceTest(employee, client);
    }
}
