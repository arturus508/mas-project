package com.example.healthfitness.controller;

import com.example.healthfitness.model.BodyStats;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.BodyStatsService;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.web.form.BodyStatsForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class BodyStatsViewController {

    private final BodyStatsService bodyStatsService;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public BodyStatsViewController(BodyStatsService bodyStatsService,
                                   UserService userService,
                                   CurrentUserService currentUserService) {
        this.bodyStatsService = bodyStatsService;
        this.userService      = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/body-stats")
    public String showBodyStats(Model model) {
        Long userId = currentUserService.id();
        User user   = userService.getUserById(userId);
        model.addAttribute("bodyStats", bodyStatsService.getBodyStatsByUser(userId));
        model.addAttribute("user", user);
        model.addAttribute("bodyStatsForm", new BodyStatsForm());
        return "body-stats";
    }

    @PostMapping("/body-stats/add")
    public String addBodyStats(@Valid @ModelAttribute("bodyStatsForm") BodyStatsForm form,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            // Redisplay the page with validation errors and existing stats
            Long userId = currentUserService.id();
            model.addAttribute("bodyStats", bodyStatsService.getBodyStatsByUser(userId));
            model.addAttribute("user", userService.getUserById(userId));
            return "body-stats";
        }
        Long userId = currentUserService.id();
        // Convert form values into entity values (BigDecimal -> double)
        BodyStats stats = new BodyStats(
                form.getDate(),
                form.getWeightKg().doubleValue(),
                form.getBodyFatPercent() != null ? form.getBodyFatPercent().doubleValue() : 0.0
        );
        bodyStatsService.addBodyStatsToUser(userId, stats);
        return "redirect:/body-stats";
    }

    // New: Delete a specific body stat.
    @GetMapping("/body-stats/delete/{id}")
    public String deleteBodyStats(@PathVariable("id") Long id) {
        bodyStatsService.deleteBodyStats(id);
        return "redirect:/body-stats";
    }
}


