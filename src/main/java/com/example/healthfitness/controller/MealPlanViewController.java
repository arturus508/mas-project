package com.example.healthfitness.controller;

import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.MealPlanService;
import com.example.healthfitness.service.MealService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.web.form.MealPlanForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Web controller for managing meal plans.  Exposes pages for
 * listing, viewing details, adding, editing and deleting meal plans.
 * All state‑mutating operations use POST requests to ensure CSRF
 * protection.  Missing resources are handled by {@link MealPlanService},
 * which throws {@link com.example.healthfitness.exception.ResourceNotFoundException}
 * when an entity is not found.  Flash messages convey success or error
 * status after redirects.
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
        this.mealPlanService    = mealPlanService;
        this.userService        = userService;
        this.mealService        = mealService;
        this.currentUserService = currentUserService;
    }

    /**
     * List all meal plans belonging to the current user.
     */
    @GetMapping
    public String listPlans(Model model) {
        Long userId = currentUserService.id();
        model.addAttribute("mealPlans", mealPlanService.getMealPlansByUser(userId));
        return "meal-plans";
    }

    /**
     * Show details for a single meal plan.  The service will throw
     * {@link com.example.healthfitness.exception.ResourceNotFoundException}
     * if the plan does not exist, which is handled by the global
     * exception handler.
     */
    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        MealPlan mealPlan = mealPlanService.getMealPlanById(id);
        model.addAttribute("mealPlan", mealPlan);
        return "meal-plan";
    }

    /**
     * Display the form to add a new meal plan.
     */
    @GetMapping("/add")
    public String addMealPlanPage(Model model) {
        model.addAttribute("mealPlanForm", new MealPlanForm());
        return "add-meal-plan";
    }

    /**
     * Persist a new meal plan.  On validation errors the user is returned
     * to the form.  On success the user is redirected back to the list
     * with a success message.
     */
    @PostMapping("/add")
    public String addMealPlan(@Valid @ModelAttribute("mealPlanForm") MealPlanForm form,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
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
        redirectAttributes.addFlashAttribute("successMessage", "Meal plan created successfully");
        return "redirect:/meal-plans";
    }

    /**
     * Display the form to edit an existing meal plan.  The service
     * will throw if the plan does not exist.  The returned view binds
     * to a {@link MealPlanForm} rather than directly to the entity.
     */
    @GetMapping("/edit/{id}")
    public String editMealPlanPage(@PathVariable Long id, Model model) {
        MealPlan mealPlan = mealPlanService.getMealPlanById(id);
        MealPlanForm form = new MealPlanForm();
        form.setPlanName(mealPlan.getPlanName());
        form.setDescription(mealPlan.getDescription());
        form.setDietaryRestriction(mealPlan.getDietaryRestriction());
        form.setStartDate(mealPlan.getStartDate());
        form.setEndDate(mealPlan.getEndDate());
        model.addAttribute("mealPlanForm", form);
        model.addAttribute("mealPlanId", id);
        return "meal-plan-edit";
    }

    /**
     * Persist updates to an existing meal plan.  On validation error the
     * user is returned to the same page.  On completion the user
     * is redirected back to the plan detail with a success message.
     */
    @PostMapping("/edit/{id}")
    public String editMealPlan(@PathVariable Long id,
                               @Valid @ModelAttribute("mealPlanForm") MealPlanForm form,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (bindingResult.hasErrors()) {
            // ensure the plan id is in the model for the form action
            model.addAttribute("mealPlanId", id);
            return "meal-plan-edit";
        }
        Long userId = currentUserService.id();
        mealPlanService.updateMealPlan(id, form, userId);
        redirectAttributes.addFlashAttribute("successMessage", "Meal plan updated successfully");
        return "redirect:/meal-plans/" + id;
    }

    /**
     * Delete an entire meal plan.  Redirect back to the list with a
     * success message.  The service will throw if the plan does not exist.
     */
    @PostMapping("/delete/{id}")
    public String deleteMealPlan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        mealPlanService.deleteMealPlan(id);
        redirectAttributes.addFlashAttribute("successMessage", "Meal plan deleted successfully");
        return "redirect:/meal-plans";
    }

    /**
     * Remove a single meal from a meal plan.  Requires POST to avoid
     * CSRF attacks; after deletion the user is redirected back to the
     * plan detail.
     */
    @PostMapping("/meals/delete/{mealId}")
    public String deleteMealFromPlan(@PathVariable Long mealId,
                                      @RequestParam("mealPlanId") Long mealPlanId,
                                      RedirectAttributes redirectAttributes) {
        mealService.deleteMeal(mealId);
        redirectAttributes.addFlashAttribute("successMessage", "Meal deleted successfully");
        return "redirect:/meal-plans/" + mealPlanId;
    }
}