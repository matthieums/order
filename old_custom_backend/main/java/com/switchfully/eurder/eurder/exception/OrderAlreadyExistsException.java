package com.switchfully.eurder.eurder.exception;

public class OrderAlreadyExistsException extends AlreadyExistsException {
    public OrderAlreadyExistsException(String existingOrderId) {
        super("Order with id " + existingOrderId + " has already been created");
    }
}
