package com.bigos.awp.exception;

/**
 * Created by bigos on 31.12.16.
 */
public class UnauthorizedChangeTaskStatus extends RuntimeException {
    public UnauthorizedChangeTaskStatus() {
        super("You are not authorized to change task status");
    }

    public UnauthorizedChangeTaskStatus(String message) {
        super(message);
    }
}
