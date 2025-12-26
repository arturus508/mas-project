package com.example.healthfitness.controller;

import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.service.MealPlanService;
import com.example.healthfitness.service.CurrentUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meal-plans") // API-specific URLs
public class MealPlanController {

    private final MealPlanService mealPlanService;
    private final CurrentUserService currentUserService;

    public MealPlanController(MealPlanService mealPlanService,
                              CurrentUserService currentUserService) {
        this.mealPlanService = mealPlanService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/{id}")
    public MealPlan getMealPlanById(@PathVariable Long id) {
        Long userId = currentUserService.id();
        return mealPlanService.getMealPlanByIdForUser(userId, id);
    }

    @PostMapping
    public MealPlan createMealPlan(@RequestBody MealPlan mealPlan) {
        Long userId = currentUserService.id();
        return mealPlanService.saveMealPlanForUser(userId, mealPlan);
    }

    @PutMapping("/{id}")
    public MealPlan updateMealPlan(@PathVariable Long id, @RequestBody MealPlan updatedMealPlan) {
        Long userId = currentUserService.id();
        return mealPlanService.updateMealPlanForUser(userId, id, updatedMealPlan);
    }

    @DeleteMapping("/{id}")
    public void deleteMealPlan(@PathVariable Long id) {
        Long userId = currentUserService.id();
        mealPlanService.deleteMealPlanForUser(userId, id);
    }
}





