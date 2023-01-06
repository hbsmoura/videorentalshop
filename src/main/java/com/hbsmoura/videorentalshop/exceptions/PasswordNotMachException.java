package com.hbsmoura.videorentalshop.exceptions;

public class PasswordNotMachException extends RuntimeException {

    public PasswordNotMachException() {
        super("The given current password does not match with the saved password");
    }
}
