package com.example.security.exceptions;

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String message) {
        super(message);
    }
}
