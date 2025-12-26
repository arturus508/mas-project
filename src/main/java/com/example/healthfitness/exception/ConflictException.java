package com.example.healthfitness.exception;

/**
 * Exception thrown when a request conflicts with existing data,
 * such as attempting to create a resource that already exists.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
