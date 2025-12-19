package com.example.healthfitness.controller;

import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.service.MealPlanService;
import com.example.healthfitness.service.MealService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.web.form.MealPlanForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Web controller for managing meal plans.  This controller exposes pages
 * for listing, adding, editing and deleting meal plans.  It relies on
 * {@link CurrentUserService} to obtain the id of the currently logged‑in
 * user rather than reading from {@code SecurityContextHolder}.  All
 * modifications are performed via the {@link MealPlanService} and
 * relationships to the current user are managed explicitly through
 * {@link UserService}.
 */
@Controller
@RequestMapping("/meal-plans")
public class MealPlanViewController {

    private final MealPlanService mealPlanService;
    private final UserService userService;
    private final MealService mealService;
    private final CurrentUserService currentUserService;

    public MealPlanViewController(MealPlanService mealPlanService,
                                  UserService userService,
                                  MealService mealService,
                                  CurrentUserService currentUserService) {
        this.mealPlanService   = mealPlanService;
        this.userService       = userService;
        this.mealService       = mealService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String listPlans(Model model) {
        Long userId = currentUserService.id();
        model.addAttribute("mealPlans", mealPlanService.getMealPlansByUser(userId));
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
        model.addAttribute("mealPlanForm", new MealPlanForm());
        return "add-meal-plan";
    }

    @PostMapping("/add")
    public String addMealPlan(@Valid @ModelAttribute("mealPlanForm") MealPlanForm form,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "add-meal-plan";
        }
        Long userId = currentUserService.id();
        MealPlan mealPlan = new MealPlan();
        mealPlan.setPlanName(form.getPlanName());
        mealPlan.setDescription(form.getDescription());
        mealPlan.setDietaryRestriction(form.getDietaryRestriction());
        mealPlan.setStartDate(form.getStartDate());
        mealPlan.setEndDate(form.getEndDate());
        mealPlan.setUser(userService.getUserById(userId));
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
    public String editMealPlan(@PathVariable Long id,
                               @ModelAttribute MealPlan updatedMealPlan) {
        // Associate the updated plan with the current user.  Without
        // explicitly setting the user here the plan could become
        // detached from the currently authenticated user.
        Long userId = currentUserService.id();
        updatedMealPlan.setUser(userService.getUserById(userId));
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