package com.switchfully.eurder.eurder.exception;

public class ItemAlreadyExistsException extends AlreadyExistsException {
    public ItemAlreadyExistsException(String alreadyExistingItem) {
        super("An item with this name already exists: " + alreadyExistingItem);
    }
}
