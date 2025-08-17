package com.example.healthfitness.controller;

import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.service.MealPlanService;
import com.example.healthfitness.service.MealService;
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
    private final MealService mealService;

    public MealPlanViewController(MealPlanService mealPlanService,
                                  UserService userService,
                                  MealService mealService) {
        this.mealPlanService = mealPlanService;
        this.userService = userService;
        this.mealService = mealService;
    }

    
    @GetMapping
    public String listPlans(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found: " + email));
        model.addAttribute("mealPlans", mealPlanService.getMealPlansByUser(user.getUserId()));
        return "meal-plans";
    }

    
    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        MealPlan mealPlan = mealPlanService.getMealPlanById(id);
        if (mealPlan == null) {
            throw new RuntimeException("MealPlan not found with id: " + id);
        }
        model.addAttribute("mealPlan", mealPlan);
        return "meal-plan";
    }

  
    @GetMapping("/add")
    public String addMealPlanPage(Model model) {
        model.addAttribute("mealPlan", new MealPlan());
        return "add-meal-plan";
    }

   
    @PostMapping("/add")
    public String addMealPlan(@ModelAttribute MealPlan mealPlan) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(email).orElseThrow();
        mealPlan.setUser(user);
        mealPlanService.saveMealPlan(mealPlan);
        return "redirect:/meal-plans";
    }

    
    @GetMapping("/edit/{id}")
    public String editMealPlanPage(@PathVariable Long id, Model model) {
        MealPlan mealPlan = mealPlanService.getMealPlanById(id);
        if (mealPlan == null) {
            throw new RuntimeException("MealPlan not found with id: " + id);
        }
        model.addAttribute("mealPlan", mealPlan);
        return "meal-plan-edit";
    }

    @PostMapping("/edit/{id}")
    public String editMealPlan(@PathVariable Long id, @ModelAttribute MealPlan updatedMealPlan) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(email).orElseThrow();
        updatedMealPlan.setUser(user);
        mealPlanService.updateMealPlan(id, updatedMealPlan);
        return "redirect:/meal-plans/" + id; 
    }

    
    @GetMapping("/delete/{id}")
    public String deleteMealPlan(@PathVariable Long id) {
        mealPlanService.deleteMealPlan(id);
        return "redirect:/meal-plans";
    }

    // Legacy: /meal-plans/{id}/meals
    @GetMapping("/{id}/meals")
    public String legacyMeals(@PathVariable Long id) {
        return "redirect:/meal-plans/" + id;
    }

    
    @GetMapping("/meals/delete/{mealId}")
    public String deleteMealFromPlan(@PathVariable Long mealId,
                                     @RequestParam("mealPlanId") Long mealPlanId) {
        mealService.deleteMeal(mealId);
        return "redirect:/meal-plans/" + mealPlanId;
    }
}
