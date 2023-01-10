package com.hbsmoura.videorentalshop.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
