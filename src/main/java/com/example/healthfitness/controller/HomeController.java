package com.example.healthfitness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        return "redirect:/today";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "redirect:/today";
    }
}
