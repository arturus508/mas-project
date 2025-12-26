package com.example.healthfitness.config;

import com.example.healthfitness.exception.ConflictException;
import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.security.access.AccessDeniedException;

/**
 * Centralized exception handler for the web layer. This class
 * intercepts exceptions thrown from controllers and returns
 * appropriate error views with helpful messages.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle resource not found exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error-404";
    }

    /**
     * Handle access denied situations.
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbidden(ForbiddenException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error-403";
    }

    /**
     * Handle security access denied exceptions.
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        model.addAttribute("message", "Access denied");
        return "error-403";
    }

    /**
     * Handle conflicts such as duplicate resources.
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflict(ConflictException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error-409";
    }

    /**
     * Handle requests to unmapped URLs to provide a custom 404 page.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("message", "Page not found");
        return "error-404";
    }

    /**
     * Handle validation errors not captured by BindingResult.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, IllegalArgumentException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage() != null ? ex.getMessage() : "Invalid request");
        return "error-400";
    }

    /**
     * Catch all other exceptions and return a 500 page.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage() != null ? ex.getMessage() : "Unexpected error");
        return "error-500";
    }
}
