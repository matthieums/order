package com.switchfully.order.exception;

public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(String additionalContext) {
        super(additionalContext);
    }
}
