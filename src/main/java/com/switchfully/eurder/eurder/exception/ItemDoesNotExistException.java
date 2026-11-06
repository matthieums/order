package com.switchfully.eurder.eurder.exception;

public class ItemDoesNotExistException extends DoesNotExistException {
    public ItemDoesNotExistException(Long itemId) {
        super("Item with id " + itemId + " is not in stock.");
    }
}
