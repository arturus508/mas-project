package com.example.healthfitness.exception;

/**
 * Exception thrown when a requested resource cannot be found.  By
 * defining a dedicated exception type we can tailor error handling
 * and return a 404 view rather than a generic error page.  The
 * exception message should contain a human‑readable description of
 * what resource is missing (e.g. "MealPlan with id 42 not found").
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}