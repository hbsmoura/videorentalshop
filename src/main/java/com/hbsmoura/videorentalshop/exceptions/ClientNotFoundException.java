package com.hbsmoura.videorentalshop.exceptions;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException() {
        super("There is no Client with the informed Id");
    }

    public ClientNotFoundException(String msg) {
        super(msg);
    }
}
