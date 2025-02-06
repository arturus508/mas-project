package com.example.healthfitness.controller;

import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.MealPlanService;
import com.example.healthfitness.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/meal-plans")
public class MealPlanViewController {

    private final MealPlanService mealPlanService;
    private final UserService userService;

    public MealPlanViewController(MealPlanService mealPlanService, UserService userService) {
        this.mealPlanService = mealPlanService;
        this.userService = userService;
    }

    // List meal plans for the current user
    @GetMapping
    public String getMealPlans(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        model.addAttribute("mealPlans", mealPlanService.getMealPlansByUser(user.getUserId()));
        return "meal-plans";
    }

    // Display the "Add Meal Plan" page
    @GetMapping("/add")
    public String addMealPlanPage(Model model) {
        model.addAttribute("mealPlan", new MealPlan());
        return "add-meal-plan";
    }

    // Handle new Meal Plan submission
    @PostMapping("/add")
    public String addMealPlan(@ModelAttribute MealPlan mealPlan) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        mealPlan.setUser(user);
        mealPlanService.saveMealPlan(mealPlan);
        return "redirect:/meal-plans";
    }

    // Display the Edit Meal Plan page (this is the key update)
    @GetMapping("/edit/{id}")
    public String editMealPlanPage(@PathVariable Long id, Model model) {
        MealPlan mealPlan = mealPlanService.getMealPlanById(id);
        if (mealPlan == null) {
            throw new RuntimeException("MealPlan not found with id: " + id);
        }
        // Add the mealPlan object (which contains its ID and meals) to the model.
        model.addAttribute("mealPlan", mealPlan);
        return "meal-plan-edit";
    }

    // Handle Meal Plan edit submission
    @PostMapping("/edit/{id}")
    public String editMealPlan(@PathVariable Long id, @ModelAttribute MealPlan updatedMealPlan) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        updatedMealPlan.setUser(user);
        mealPlanService.updateMealPlan(id, updatedMealPlan);
        return "redirect:/meal-plans";
    }

    // Delete a Meal Plan
    @GetMapping("/delete/{id}")
    public String deleteMealPlan(@PathVariable Long id) {
        mealPlanService.deleteMealPlan(id);
        return "redirect:/meal-plans";
    }
}




