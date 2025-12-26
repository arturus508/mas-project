package com.example.healthfitness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class LegacyRoutesController {

    @GetMapping({"/meals/save", "/meals/new"})
    public String legacyMealsGet() {
        String today = LocalDate.now().toString();
        return "redirect:/meals?date=" + today;
    }

    @PostMapping("/meals/save")
    public String legacyMealsPost() {
        String today = LocalDate.now().toString();
        return "redirect:/meals?date=" + today;
    }

    @GetMapping("/meal-plans/day")
    public String legacyMealPlansDay(@RequestParam(value = "date", required = false) String date) {
        String target = date == null || date.isBlank() ? LocalDate.now().toString() : date;
        return "redirect:/meals?date=" + target;
    }
}
