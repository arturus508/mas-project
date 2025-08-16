package com.example.healthfitness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        // Landing / login page (jeśli masz index.html)
        model.addAttribute("pageTitle", "Welcome");
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Prosty dashboard bez Guides; później dołożymy tu podsumowanie snu itd.
        model.addAttribute("pageTitle", "Dashboard");
        return "dashboard";
    }
}


