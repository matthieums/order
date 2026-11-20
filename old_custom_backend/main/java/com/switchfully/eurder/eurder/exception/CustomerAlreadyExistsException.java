package com.switchfully.eurder.eurder.exception;

public class CustomerAlreadyExistsException extends AlreadyExistsException {
    public CustomerAlreadyExistsException(String alreadyExistingMail) {
        super("A customer with this email already exists: " + alreadyExistingMail);
    }
}
