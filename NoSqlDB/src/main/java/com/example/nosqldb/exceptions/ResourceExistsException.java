package com.example.nosqldb.exceptions;

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String message) {
        super(message);
    }
}
