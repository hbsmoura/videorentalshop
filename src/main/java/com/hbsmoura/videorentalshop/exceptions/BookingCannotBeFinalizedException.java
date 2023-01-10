package com.hbsmoura.videorentalshop.exceptions;

public class BookingCannotBeFinalizedException extends RuntimeException {

    public BookingCannotBeFinalizedException() {
        super("The selected booking cannot be finalized. Only booking with requested or rented state can be finalized");
    }

    public BookingCannotBeFinalizedException(String msg) {
        super(msg);
    }
}
