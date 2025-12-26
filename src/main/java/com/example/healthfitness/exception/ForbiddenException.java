package com.example.healthfitness.exception;

/**
 * Exception thrown when a user attempts to access or modify a resource
 * they do not own or are not authorized to access.
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
