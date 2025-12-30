package com.example.healthfitness.controller;

import com.example.healthfitness.model.Ingredient;
import com.example.healthfitness.model.Meal;
import com.example.healthfitness.service.IngredientService;
import com.example.healthfitness.service.MealService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/meals")
public class IngredientViewController {

    private final IngredientService ingredientService;
    private final MealService mealService;

    public IngredientViewController(IngredientService ingredientService, MealService mealService) {
        this.ingredientService = ingredientService;
        this.mealService = mealService;
    }

    // Display the list of Ingredients attached to a specific Meal
    @GetMapping("/{mealId}/ingredients")
    public String listIngredientsForMeal(@PathVariable Long mealId, Model model) {
        Meal meal = mealService.getMealById(mealId);
        model.addAttribute("meal", meal);
        model.addAttribute("ingredients", ingredientService.getIngredientsByMealId(mealId));
        model.addAttribute("mealId", mealId);
        return "ingredient-list";  // Thymeleaf template
    }

    // Show the form to add a new Ingredient to a Meal
    @GetMapping("/{mealId}/ingredients/add")
    public String addIngredientPage(@PathVariable Long mealId, Model model) {
        model.addAttribute("ingredient", new Ingredient());
        model.addAttribute("mealId", mealId);
        return "ingredient-form";  // Thymeleaf template
    }

    // Process the form submission to add a new Ingredient to the Meal
    @PostMapping("/{mealId}/ingredients/add")
    public String addIngredient(@PathVariable Long mealId, @ModelAttribute Ingredient ingredient) {
        Meal meal = mealService.getMealById(mealId);
        // Set the Meal on the Ingredient
        ingredient.setMeal(meal);
        ingredientService.saveIngredient(ingredient);
        return "redirect:/meals/" + mealId + "/ingredients";
    }

    // Remove an Ingredient from the Meal
    @PostMapping("/{mealId}/ingredients/delete/{ingredientId}")
    public String removeIngredient(@PathVariable Long mealId, @PathVariable Long ingredientId) {
        ingredientService.deleteIngredient(ingredientId);
        return "redirect:/meals/" + mealId + "/ingredients";
    }
}





