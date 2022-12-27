package com.hbsmoura.videorentalshop.exceptions;

public class MovieNotAvailableException extends RuntimeException {

    public MovieNotAvailableException() {
        super("The selected movie is not available right now. Please, try again later");
    }
}
