package com.example.healthfitness.controller;

import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.SectionVisibilityService;
import com.example.healthfitness.web.view.SectionVisibilityItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final CurrentUserService currentUserService;
    private final SectionVisibilityService sectionVisibilityService;

    public SettingsController(CurrentUserService currentUserService,
                              SectionVisibilityService sectionVisibilityService) {
        this.currentUserService = currentUserService;
        this.sectionVisibilityService = sectionVisibilityService;
    }

    @GetMapping
    public String showSettingsPage(Model model) {
        Long userId = currentUserService.id();
        List<SectionVisibilityItem> sections = sectionVisibilityService.getSections(userId);
        model.addAttribute("todayBodySections", sections.stream()
                .filter(s -> "body".equals(s.getMode()) && "today".equals(s.getRange()))
                .toList());
        model.addAttribute("todayMindSections", sections.stream()
                .filter(s -> "mind".equals(s.getMode()) && "today".equals(s.getRange()))
                .toList());
        model.addAttribute("weekBodySections", sections.stream()
                .filter(s -> "body".equals(s.getMode()) && "week".equals(s.getRange()))
                .toList());
        model.addAttribute("weekMindSections", sections.stream()
                .filter(s -> "mind".equals(s.getMode()) && "week".equals(s.getRange()))
                .toList());
        return "settings";
    }

    @PostMapping("/sections/{key}")
    public String updateSectionVisibility(@PathVariable String key,
                                          @RequestParam boolean hidden) {
        Long userId = currentUserService.id();
        sectionVisibilityService.setHidden(userId, key, hidden);
        return "redirect:/settings";
    }
}

