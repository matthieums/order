package com.switchfully.eurder.eurder.exception;


public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("User with id: "+  id + " was not found.");
    }
}
