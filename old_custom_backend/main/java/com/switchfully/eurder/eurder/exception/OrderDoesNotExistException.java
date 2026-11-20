package com.switchfully.eurder.eurder.exception;

public class OrderDoesNotExistException extends DoesNotExistException {
    public OrderDoesNotExistException(Long id) {
        super("Order with id " + id + " was not found.");
    }
}
