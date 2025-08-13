package com.example.healthfitness.controller;

import com.example.healthfitness.model.Guide;
import com.example.healthfitness.model.GuideCategory;
import com.example.healthfitness.service.GuideService;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/guides")
public class GuideViewController {

    @Autowired
    private GuideService guideService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listGuides(@RequestParam(name = "category", required = false) GuideCategory category,
                             Model model) {
        var cat = category != null ? category : GuideCategory.MIND;
        List<Guide> guides = guideService.listByCategory(cat);
        model.addAttribute("guides", guides);
        model.addAttribute("category", cat);
        return "guides";
    }

    @GetMapping("/{slug}")
    public String guideDetail(@PathVariable String slug, Model model) {
        var guide = guideService.getBySlug(slug);
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var email = auth != null ? auth.getName() : null;
        if (email != null) {
            var user = userService.findByEmail(email).orElse(null);
            if (user != null) guideService.touchLastViewed(guide.getId(), user.getUserId());
        }
        model.addAttribute("guide", guide);
        return "guide-detail";
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var user  = userService.findByEmail(email).orElseThrow();
        guideService.markCompleted(id, user.getUserId());
        return "redirect:/guides";
    }
}
