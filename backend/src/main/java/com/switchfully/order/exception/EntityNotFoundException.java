package com.switchfully.order.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String additionalContext, Class classOfEntity, UUID id) {
        super("During " + additionalContext + ", the following entity was not found: "
                + classOfEntity.getSimpleName() + " with id = " + id.toString());
    }
}
