package com.switchfully.eurder.eurder.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("User not authenticated.");
    }
}
