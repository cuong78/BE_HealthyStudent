package com.healthy.backend.exception;

public class AuthorizeException extends RuntimeException {
    public AuthorizeException(String message) {

        super(message);
    }
}
