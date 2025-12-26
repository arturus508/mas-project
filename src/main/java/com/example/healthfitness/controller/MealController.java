package com.example.healthfitness.controller;

import com.example.healthfitness.model.Meal;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.MealService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for meal management.  This refactored version
 * decouples user resolution from the security context by using
 * {@link CurrentUserService}.  It exposes endpoints for listing meals
 * associated with a meal plan, adding a new meal to a plan and
 * deleting a meal.  The {@link MealService} performs the actual
 * persistence operations.
 */
@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;
    private final CurrentUserService currentUserService;

    public MealController(MealService mealService,
                          CurrentUserService currentUserService) {
        this.mealService = mealService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/meal-plan/{mealPlanId}")
    public List<Meal> getMealsForMealPlan(@PathVariable Long mealPlanId) {
        Long userId = currentUserService.id();
        return mealService.getMealsByMealPlan(userId, mealPlanId);
    }

    @PostMapping("/meal-plan/{mealPlanId}/add")
    public Meal addMeal(@PathVariable Long mealPlanId, @RequestBody Meal meal) {
        Long userId = currentUserService.id();
        return mealService.saveMeal(meal, userId, mealPlanId);
    }

    @DeleteMapping("/delete/{mealId}")
    public void deleteMeal(@PathVariable Long mealId) {
        Long userId = currentUserService.id();
        mealService.deleteMeal(userId, mealId);
    }
}
