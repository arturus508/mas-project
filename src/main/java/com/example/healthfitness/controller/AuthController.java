package com.example.healthfitness.controller;

import com.example.healthfitness.exception.ConflictException;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.web.form.UserRegistrationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Show the registration form (GET /register)
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerForm", new UserRegistrationForm());
        return "register"; // register.html in templates
    }

    // Handle form submission for registration (POST /register)
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") UserRegistrationForm form,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        String email = form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null;
        form.setEmail(email);
        if (email == null || email.isBlank()) {
            bindingResult.rejectValue("email", "email.invalid", "Email is required");
            return "register";
        }
        if (userService.findByEmail(email).isPresent()) {
            bindingResult.rejectValue("email", "email.duplicate", "Email already exists");
            return "register";
        }

        try {
            userService.registerUser(form.getName(), email, form.getPassword());
        } catch (ConflictException ex) {
            bindingResult.rejectValue("email", "email.duplicate", "Email already exists");
            return "register";
        }
        // After success, redirect to /login with a flag
        return "redirect:/login?registered=true";
    }
}







