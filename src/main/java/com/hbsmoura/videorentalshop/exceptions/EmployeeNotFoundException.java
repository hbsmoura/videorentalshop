package com.hbsmoura.videorentalshop.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException() {
        super("There is no Employee with the informed Id");
    }
}
