package com.switchfully.eurder.eurder.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("You don't have the permission to access this resource.");
    }
}
