package com.hbsmoura.videorentalshop.exceptions;

public class BookingCannotBeUpdatedFromRequestedException extends RuntimeException {

    public enum Operation {
        CANCELED("canceled"),
        STARTED("started");

        Operation(String started) {}
    }
    public BookingCannotBeUpdatedFromRequestedException(Operation operation) {
        super("This booking cannot be "+ operation.name() +". Only requested bookings can be " + operation.name());
    }
}
