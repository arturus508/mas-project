package com.example.healthfitness.controller;

import com.example.healthfitness.model.User;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // 1. Get current email from security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName(); // The logged-in user's email

        // 2. Find the actual User in the DB
        User user = userService.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentEmail));

        // 3. Put the user in the model so Thymeleaf can do user.userId
        model.addAttribute("user", user);

        // 4. Render dashboard.html
        return "dashboard";
    }
}

