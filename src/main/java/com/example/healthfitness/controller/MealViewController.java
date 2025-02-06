package com.example.healthfitness.controller;


import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.MealPlanService;
import com.example.healthfitness.service.MealService;
import com.example.healthfitness.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/meal-plans")
public class MealViewController {

    private final MealService mealService;
    private final UserService userService;
    private final MealPlanService mealPlanService;

    public MealViewController(MealService mealService, UserService userService, MealPlanService mealPlanService) {
        this.mealService = mealService;
        this.userService = userService;
        this.mealPlanService = mealPlanService;
    }

    // Show the Meal Plan Details page (with its list of Meals)
    @GetMapping("/{id}/meals")
    public String getMealsForPlan(@PathVariable Long id, Model model) {
        MealPlan mealPlan = mealPlanService.getMealPlanById(id);
        if (mealPlan == null) {
            throw new RuntimeException("MealPlan not found with id: " + id);
        }
        model.addAttribute("mealPlan", mealPlan);
        model.addAttribute("meals", mealService.getMealsByMealPlan(id));
        model.addAttribute("mealPlanId", id);
        return "meal-plan";  // corresponds to meal-plan.html
    }

    // Display the Add Meal form.
    @GetMapping("/{id}/meals/add")
    public String addMealPage(@PathVariable Long id, Model model) {
        model.addAttribute("meal", new Meal());
        model.addAttribute("mealPlanId", id);
        return "meal-form";  // corresponds to meal-form.html
    }

    // Process the Add Meal form submission.
    @PostMapping("/{id}/meals/add")
    public String addMeal(@PathVariable Long id, @ModelAttribute Meal meal) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        mealService.saveMeal(meal, user.getUserId(), id);
        return "redirect:/meal-plans/" + id + "/meals";
    }

    // Delete a Meal (note: now mapped under /meal-plans/{mealPlanId}/meals/delete/{mealId})
    @GetMapping("/{mealPlanId}/meals/delete/{mealId}")
    public String deleteMeal(@PathVariable("mealPlanId") Long mealPlanId,
                             @PathVariable("mealId") Long mealId) {
        mealService.deleteMeal(mealId);
        return "redirect:/meal-plans/" + mealPlanId + "/meals";
    }

    // ===== NEW: Edit Meal Endpoints =====

    // Display the Edit Meal form.
    @GetMapping("/{mealPlanId}/meals/edit/{mealId}")
    public String editMealPage(@PathVariable("mealPlanId") Long mealPlanId,
                               @PathVariable("mealId") Long mealId,
                               Model model) {
        Meal meal = mealService.getMealById(mealId);
        if (meal == null) {
            throw new RuntimeException("Meal not found with id: " + mealId);
        }
        model.addAttribute("meal", meal);
        model.addAttribute("mealPlanId", mealPlanId);
        return "meal-edit";  // corresponds to meal-edit.html
    }

    // Process the Edit Meal form submission.
    @PostMapping("/{mealPlanId}/meals/edit/{mealId}")
    public String editMeal(@PathVariable("mealPlanId") Long mealPlanId,
                           @PathVariable("mealId") Long mealId,
                           @ModelAttribute Meal meal) {
        // Ensure the meal remains linked to the correct MealPlan.
        MealPlan mealPlan = mealPlanService.getMealPlanById(mealPlanId);
        meal.setMealId(mealId);
        meal.setMealPlan(mealPlan);
        mealService.updateMeal(meal);
        return "redirect:/meal-plans/" + mealPlanId + "/meals";
    }
}


