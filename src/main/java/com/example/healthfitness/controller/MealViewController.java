package com.example.healthfitness.controller;

import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.model.MealType;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.MealPlanRepository;
import com.example.healthfitness.repository.MealRepository;
import com.example.healthfitness.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

@Controller
public class MealViewController {

    private final MealRepository meals;
    private final MealPlanRepository plans;
    private final UserService userService;

    public MealViewController(MealRepository meals,
                              MealPlanRepository plans,
                              UserService userService) {
        this.meals = meals;
        this.plans = plans;
        this.userService = userService;
    }

    @GetMapping("/meals/add")
    public String addForm(@RequestParam(value = "date", required = false) String dateStr,
                          @RequestParam(value = "type", required = false) String type,
                          @RequestParam(value = "mealPlanId", required = false) Long mealPlanId,
                          Model model) {
        LocalDate date = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();
        model.addAttribute("date", date);
        model.addAttribute("type", type);
        model.addAttribute("types", Arrays.asList(MealType.values()));
        model.addAttribute("mealPlanId", mealPlanId);
        return "meals-add";
    }

    @PostMapping("/meals/add")
    public String save(@RequestParam(value = "date", required = false) String dateStr,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "kcal", required = false) Integer kcal,
                       @RequestParam(value = "protein", required = false) Integer protein,
                       @RequestParam(value = "fat", required = false) Integer fat,
                       @RequestParam(value = "carbs", required = false) Integer carbs,
                       @RequestParam(value = "mealPlanId", required = false) Long mealPlanId) {

        LocalDate date = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();
        String typeStr = (type != null && !type.isBlank()) ? type : MealType.BREAKFAST.name();
        MealType mealType = MealType.valueOf(typeStr);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User u = userService.findByEmail(email).orElseThrow();

        Meal m = new Meal();
        m.setUser(u);
        m.setDate(date);
        m.setMealType(mealType);
        m.setKcalManual(kcal);
        m.setProteinManual(protein);
        m.setFatManual(fat);
        m.setCarbsManual(carbs);

        if (mealPlanId != null) {
            MealPlan plan = plans.findById(mealPlanId).orElse(null);
            m.setMealPlan(plan);
        }

        meals.save(m);

        if (mealPlanId != null) {
            return "redirect:/meal-plans/edit/" + mealPlanId;
        }
        return "redirect:/meal-plans/day?date=" + date;
    }
}

