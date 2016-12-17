package com.bigos.awp.exception;

/**
 * Created by bigos on 04.12.16.
 */

public class EntityNotFoundException extends RuntimeException {

    private long entityId;

    public EntityNotFoundException(long entityId) {
        this.entityId = entityId;
    }

    public long getUserId() {
        return entityId;
    }
}
