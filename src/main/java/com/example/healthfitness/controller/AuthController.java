package com.example.healthfitness.controller;

import com.example.healthfitness.model.User;
import com.example.healthfitness.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Show the registration form (GET /register)
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // register.html in templates
    }

    // Handle form submission for registration (POST /register)
    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        Optional<User> existingUser = userService.findByEmail(email);
        if (existingUser.isPresent()) {
            // If user with this email already exists, show error
            model.addAttribute("error", "User already exists!");
            return "register";
        }

        userService.registerUser(name, email, password);
        // After success, redirect to /login with a flag
        return "redirect:/login?registered=true";
    }
}







