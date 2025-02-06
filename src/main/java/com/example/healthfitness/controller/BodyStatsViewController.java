package com.example.healthfitness.controller;

import com.example.healthfitness.model.BodyStats;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.BodyStatsService;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BodyStatsViewController {

    @Autowired
    private BodyStatsService bodyStatsService;

    @Autowired
    private UserService userService;

    @GetMapping("/body-stats")
    public String showBodyStats(Model model) {
        // Get current user from Security Context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // Retrieve that user's body stats and add to model.
        model.addAttribute("bodyStats", bodyStatsService.getBodyStatsByUser(user.getUserId()));
        model.addAttribute("user", user);
        return "body-stats"; // corresponds to body-stats.html
    }

    @PostMapping("/body-stats/add")
    public String addBodyStats(@RequestParam String dateRecorded,
                               @RequestParam double weight,
                               @RequestParam double bodyFatPercent) {
        // Get current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // Create BodyStats & associate with user
        BodyStats stats = new BodyStats(dateRecorded, weight, bodyFatPercent);
        bodyStatsService.addBodyStatsToUser(user.getUserId(), stats);
        return "redirect:/body-stats";
    }

    // New: Delete a specific body stat.
    @GetMapping("/body-stats/delete/{id}")
    public String deleteBodyStats(@PathVariable("id") Long id) {
        bodyStatsService.deleteBodyStats(id);
        return "redirect:/body-stats";
    }
}


