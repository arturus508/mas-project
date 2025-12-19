package com.example.healthfitness.controller;

import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealType;
import com.example.healthfitness.repository.MealPlanRepository;
import com.example.healthfitness.repository.MealRepository;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.CurrentUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller for manual meal entry.  Dates and meal types are bound
 * directly to {@link LocalDate} and {@link MealType} via Spring's
 * {@link RequestParam} conversion.  The current user id is obtained
 * through {@link CurrentUserService} rather than reading from the
 * security context.  If no date or type is provided, sensible defaults
 * (today and BREAKFAST) are used.
 */
@Controller
@RequestMapping("/meals")
public class MealViewController {

    private final MealRepository meals;
    private final MealPlanRepository plans;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public MealViewController(MealRepository meals,
                              MealPlanRepository plans,
                              UserService userService,
                              CurrentUserService currentUserService) {
        this.meals = meals;
        this.plans = plans;
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/add")
    public String addForm(Model model,
                          @RequestParam(value = "mealPlanId", required = false) Long mealPlanId,
                          @RequestParam(value = "date", required = false)
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        model.addAttribute("mealPlanId", mealPlanId);
        model.addAttribute("date", date != null ? date : LocalDate.now());
        return "meals-add";
    }

    @PostMapping("/add")
    public String save(@RequestParam(value = "date", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       @RequestParam(value = "type", required = false) MealType type,
                       @RequestParam(value = "kcal", required = false) Integer kcal,
                       @RequestParam(value = "protein", required = false) Integer protein,
                       @RequestParam(value = "fat", required = false) Integer fat,
                       @RequestParam(value = "carbs", required = false) Integer carbs,
                       @RequestParam(value = "mealPlanId", required = false) Long mealPlanId) {
        LocalDate actualDate = date != null ? date : LocalDate.now();
        MealType mealType = type != null ? type : MealType.BREAKFAST;
        Long userId = currentUserService.id();
        var user = userService.getUserById(userId);
        Meal m = new Meal();
        m.setUser(user);
        m.setDate(actualDate);
        m.setMealType(mealType);
        m.setKcalManual(kcal);
        m.setProteinManual(protein);
        m.setFatManual(fat);
        m.setCarbsManual(carbs);
        if (mealPlanId != null) {
            plans.findById(mealPlanId).ifPresent(m::setMealPlan);
        }
        meals.save(m);
        if (mealPlanId != null) {
            return "redirect:/meal-plans/" + mealPlanId;
        }
        return "redirect:/meal-plans/day?date=" + actualDate;
    }
}