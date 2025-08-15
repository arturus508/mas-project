package com.example.healthfitness.controller;

import com.example.healthfitness.service.SleepService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.GuideService;
import com.example.healthfitness.model.Guide;
import com.example.healthfitness.model.GuideCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private SleepService sleepService;

    @Autowired
    private GuideService guideService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var email = auth != null ? auth.getName() : null;

        Long userId = null;
        if (email != null) {
            var user = userService.findByEmail(email).orElse(null);
            if (user != null) userId = user.getUserId();
        }

        var yesterdayText = "No data";
        if (userId != null) {
            var y = LocalDate.now().minusDays(1);
            var se = sleepService.getYesterday(userId);
            if (se.isPresent()) {
                var e = se.get();
                var mins = java.time.Duration.between(e.getSleepStart(), e.getSleepEnd());
                if (mins.isNegative()) mins = mins.plusDays(1);
                yesterdayText = mins.toHours() + "h " + mins.toMinutesPart() + "m  ·  q" + e.getQuality();
            }
        }

        List<Guide> mindRecs = Collections.emptyList();
        if (userId != null) {
            mindRecs = guideService.recommendMindGuides(userId, 2);
        }

        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("yesterdaySleep", yesterdayText);
        model.addAttribute("mindGuides", mindRecs);

        return "dashboard";
    }
}

