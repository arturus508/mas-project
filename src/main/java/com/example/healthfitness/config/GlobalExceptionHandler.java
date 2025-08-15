package com.example.healthfitness.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        return "error-simple";
    }

    @ExceptionHandler(Exception.class)
    public String handleAny(Exception ex, Model model){
        model.addAttribute("message", ex.getMessage() != null ? ex.getMessage() : "Unexpected error");
        return "error-simple";
    }
}
