package com.hbsmoura.videorentalshop.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserIdNotFoundException extends AuthenticationException {

    public UserIdNotFoundException(String message) {
        super(message);
    }
}
