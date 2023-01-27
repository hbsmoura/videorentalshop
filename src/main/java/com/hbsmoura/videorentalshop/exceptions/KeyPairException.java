package com.hbsmoura.videorentalshop.exceptions;

public class KeyPairException extends RuntimeException{

    public KeyPairException() {
        super("Error loading the security key pair");
    }

    public KeyPairException(String message) {
        super(message);
    }
}
