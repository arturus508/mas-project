package com.example.healthfitness.controller;

import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileViewController {

    private final CurrentUserService currentUserService;
    private final UserService userService;

    public ProfileViewController(CurrentUserService currentUserService,
                                 UserService userService) {
        this.currentUserService = currentUserService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Long userId = currentUserService.id();
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("pageTitle", "Profile");
        return "profile";
    }
}
