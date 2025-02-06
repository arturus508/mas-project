package com.example.healthfitness.controller;

import com.example.healthfitness.model.Meal;
import com.example.healthfitness.service.MealService;
import com.example.healthfitness.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;
    private final UserService userService;

    public MealController(MealService mealService, UserService userService) {
        this.mealService = mealService;
        this.userService = userService;
    }





    @GetMapping("/meal-plan/{mealPlanId}")
    public List<Meal> getMealsForMealPlan(@PathVariable Long mealPlanId) {
        return mealService.getMealsByMealPlan(mealPlanId);
    }


    @PostMapping("/meal-plan/{mealPlanId}/add")
    public Meal addMeal(@PathVariable Long mealPlanId, @RequestBody Meal meal) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getUserId();

        return mealService.saveMeal(meal, userId, mealPlanId);
    }



    @DeleteMapping("/delete/{mealId}")
    public void deleteMeal(@PathVariable Long mealId) {
        mealService.deleteMeal(mealId);
    }
}
