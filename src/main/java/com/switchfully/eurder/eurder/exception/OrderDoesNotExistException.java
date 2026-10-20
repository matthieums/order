package com.switchfully.eurder.eurder.exception;

public class OrderDoesNotExistException extends DoesNotExistException {
    public OrderDoesNotExistException(String id) {
        super("Order with id " + id + " was not found.");
    }
}
