package com.example.healthfitness.controller;

import com.example.healthfitness.model.User;
import com.example.healthfitness.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // If user is already logged in, redirect to dashboard
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }
        return "login";  // Serve login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Optional<User> userOpt = userService.findByEmail(username);

        if (userOpt.isEmpty() || !userOpt.get().checkPassword(password)) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        session.setAttribute("user", userOpt.get());
        return "redirect:/dashboard";  // Redirect to dashboard after successful login
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, @RequestParam String password, Model model) {
        Optional<User> existingUser = userService.findByEmail(email);
        if (existingUser.isPresent()) {
            model.addAttribute("error", "User already exists!");
            return "register";
        }
        userService.registerUser(name, email, password);
        return "redirect:/login?registered=true";
    }

}





