package com.hbsmoura.videorentalshop.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

public class CustomUserDetailsServiceTest extends InMemoryUserDetailsManager {

    public CustomUserDetailsServiceTest() {
    }

    public CustomUserDetailsServiceTest(Collection<UserDetails> users) {
        super(users);
    }

    public CustomUserDetailsServiceTest(UserDetails... users) {
        super(users);
    }

    public CustomUserDetailsServiceTest(Properties users) {
        super(users);
    }

    public boolean isItself(UUID id) {
        return true;
    }
}
