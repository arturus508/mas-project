package com.example.healthfitness.config;

import com.example.healthfitness.exception.ResourceNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Centralized exception handler for the web layer.  This class
 * intercepts exceptions thrown from controllers and returns
 * appropriate error views with helpful messages.  By catching
 * {@link ResourceNotFoundException} we can return a 404 page when
 * domain objects are missing.  A fallback handler catches any
 * unanticipated exceptions and returns a 500 page.  The
 * {@link NoHandlerFoundException} handler ensures a proper 404 view
 * when a URL does not match any controller mapping.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle domain‑level resource not found exceptions.  These are
     * typically thrown when an entity lookup by id fails.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error-404";
    }

    /**
     * Handle requests to unmapped URLs to provide a custom 404 page.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("message", "Page not found");
        return "error-404";
    }

    /**
     * Catch all other exceptions and return a 500 page.  In a real
     * application you might log the exception or send it to an
     * error‑monitoring service.
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage() != null ? ex.getMessage() : "Unexpected error");
        return "error-500";
    }
}