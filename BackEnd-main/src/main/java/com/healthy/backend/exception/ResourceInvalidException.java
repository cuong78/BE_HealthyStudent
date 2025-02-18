package com.healthy.backend.exception;

public class ResourceInvalidException extends RuntimeException {
    public ResourceInvalidException(String message) {
        super(message);
    }
}