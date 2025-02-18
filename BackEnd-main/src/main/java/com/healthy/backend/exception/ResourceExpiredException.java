package com.healthy.backend.exception;

public class ResourceExpiredException extends RuntimeException{
    public ResourceExpiredException(String message) {
        super(message);
    }
}
