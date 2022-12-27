package com.hbsmoura.videorentalshop.exceptions;

public class MovieNotFoundException extends RuntimeException{

    public MovieNotFoundException() {
        super("There is no Movie with the informed Id");
    }
}
