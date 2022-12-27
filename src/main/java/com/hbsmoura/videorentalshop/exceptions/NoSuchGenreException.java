package com.hbsmoura.videorentalshop.exceptions;

public class NoSuchGenreException extends RuntimeException {

    public NoSuchGenreException(String text) {
        super("There is no such genre named " + text);
    }
}
