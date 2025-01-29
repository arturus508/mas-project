package com.example.healthfitness.controller;

import com.example.healthfitness.model.BodyStats;
import com.example.healthfitness.service.BodyStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BodyStatsViewController {

    @Autowired
    private BodyStatsService bodyStatsService;

    @GetMapping("/body-stats")
    public String showBodyStats(@RequestParam Long userId, Model model) {
        model.addAttribute("bodyStats", bodyStatsService.getBodyStatsByUser(userId));
        model.addAttribute("userId", userId);
        return "body-stats";
    }

    @PostMapping("/body-stats/add")
    public String addBodyStats(@RequestParam Long userId,
                               @RequestParam String dateRecorded,
                               @RequestParam double weight,
                               @RequestParam double bodyFatPercent) {
        bodyStatsService.addBodyStatsToUser(userId, new BodyStats(dateRecorded, weight, bodyFatPercent));
        return "redirect:/body-stats?userId=" + userId;
    }
}
