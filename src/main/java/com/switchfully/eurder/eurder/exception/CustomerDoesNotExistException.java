package com.switchfully.eurder.eurder.exception;

public class CustomerDoesNotExistException extends DoesNotExistException {
    public CustomerDoesNotExistException(String id) {
        super("Customer with id " + id + " does not exist.");
    }
}
