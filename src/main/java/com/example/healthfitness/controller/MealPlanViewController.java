package com.example.healthfitness.controller;

import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.service.MealPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/meal-plans") // View-specific URLs
public class MealPlanViewController {

    private final MealPlanService mealPlanService;

    public MealPlanViewController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    @GetMapping
    public String getMealPlans(Model model) {
        model.addAttribute("mealPlans", mealPlanService.getAllMealPlans());
        return "meal-plans"; // Corresponds to meal-plans.html
    }

    @GetMapping("/add")
    public String addMealPlanPage(Model model) {
        model.addAttribute("mealPlan", new MealPlan());
        return "add-meal-plan";
    }

    @PostMapping("/add")
    public String addMealPlan(@ModelAttribute MealPlan mealPlan) {
        mealPlanService.saveMealPlan(mealPlan);
        return "redirect:/meal-plans";
    }

    @GetMapping("/edit/{id}")
    public String editMealPlanPage(@PathVariable Long id, Model model) {
        model.addAttribute("mealPlan", mealPlanService.getMealPlanById(id));
        return "meal-plan-edit"; // Corresponds to meal-plan-edit.html
    }

    @PostMapping("/edit/{id}")
    public String editMealPlan(@PathVariable Long id, @ModelAttribute MealPlan updatedMealPlan) {
        mealPlanService.updateMealPlan(id, updatedMealPlan);
        return "redirect:/meal-plans";
    }




    @GetMapping("/delete/{id}")
    public String deleteMealPlan(@PathVariable Long id) {
        mealPlanService.deleteMealPlan(id);
        return "redirect:/meal-plans";
    }
}

