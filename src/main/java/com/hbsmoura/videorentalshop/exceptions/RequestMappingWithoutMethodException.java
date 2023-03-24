package com.hbsmoura.videorentalshop.exceptions;

public class RequestMappingWithoutMethodException extends RuntimeException{
    public RequestMappingWithoutMethodException() {
        super("Request mapping annotation without specified request method");
    }

    public RequestMappingWithoutMethodException(String message) {
        super(message);
    }
}
