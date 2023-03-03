package com.hbsmoura.videorentalshop.exceptions;

public class FailedLoginException extends RuntimeException{

    public FailedLoginException() {
        super("Login failed! Invalid username or password");
    }

    public FailedLoginException(String message) {
        super(message);
    }
}
