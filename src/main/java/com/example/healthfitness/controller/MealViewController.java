package com.example.healthfitness.controller;

import com.example.healthfitness.model.MealType;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.MealService;
import com.example.healthfitness.web.form.MealForm;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * MVC controller for manual meal entry.  This version binds
 * user‑supplied data to a {@link MealForm} DTO and delegates all
 * persistence logic to the {@link MealService}.  The current user id
 * is resolved via {@link CurrentUserService} rather than via the
 * security context.  Default values for date and type are set in
 * the GET handler and reused when binding fails in the POST handler.
 */
@Controller
@RequestMapping("/meals")
public class MealViewController {

    private final MealService mealService;
    private final CurrentUserService currentUserService;

    public MealViewController(MealService mealService,
                              CurrentUserService currentUserService) {
        this.mealService        = mealService;
        this.currentUserService = currentUserService;
    }

    /**
     * Display the meal add form.  Optional request parameters can
     * prefill the date and meal plan id.  Defaults are applied for
     * missing values.
     */
    @GetMapping("/add")
    public String addForm(Model model,
                          @RequestParam(value = "mealPlanId", required = false) Long mealPlanId,
                          @RequestParam(value = "date", required = false)
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        MealForm form = new MealForm();
        form.setMealPlanId(mealPlanId);
        form.setDate(date != null ? date : LocalDate.now());
        form.setType(MealType.BREAKFAST);
        model.addAttribute("mealForm", form);
        return "meals-add";
    }

    /**
     * Process the submitted meal form.  On validation errors the
     * original form is redisplayed.  On success the meal is saved and
     * the user is redirected back to the meal plan detail or daily
     * overview depending on whether a meal plan id was provided.
     */
    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("mealForm") MealForm mealForm,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "meals-add";
        }
        Long userId = currentUserService.id();
        mealService.saveMeal(mealForm, userId);
        Long mealPlanId = mealForm.getMealPlanId();
        if (mealPlanId != null) {
            return "redirect:/meal-plans/" + mealPlanId;
        }
        LocalDate date = mealForm.getDate() != null ? mealForm.getDate() : LocalDate.now();
        return "redirect:/meal-plans/day?date=" + date;
    }
}