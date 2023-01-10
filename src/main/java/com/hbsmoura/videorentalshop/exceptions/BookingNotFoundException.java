package com.hbsmoura.videorentalshop.exceptions;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException() {
        super("There is no Booking with the informed Id");
    }

    public BookingNotFoundException(String msg) {
        super(msg);
    }
}
