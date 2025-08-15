package com.example.healthfitness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class LegacyRoutesController {

    @GetMapping({"/meals/save", "/meals/new"})
    public String legacyMealsGet() {
        String today = LocalDate.now().toString();
        return "redirect:/meal-plans/day?date=" + today;
    }

    @PostMapping("/meals/save")
    public String legacyMealsPost() {
        String today = LocalDate.now().toString();
        return "redirect:/meal-plans/day?date=" + today;
    }
}
