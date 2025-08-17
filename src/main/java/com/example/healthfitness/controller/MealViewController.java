package com.example.healthfitness.controller;

import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealType;
import com.example.healthfitness.repository.MealPlanRepository;
import com.example.healthfitness.repository.MealRepository;
import com.example.healthfitness.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/meals")
public class MealViewController {

    private final MealRepository meals;
    private final MealPlanRepository plans;
    private final UserService userService;

    public MealViewController(MealRepository meals, MealPlanRepository plans, UserService userService) {
        this.meals = meals;
        this.plans = plans;
        this.userService = userService;
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
    public String save(@RequestParam(value = "date", required = false) String dateStr,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "kcal", required = false) Integer kcal,
                       @RequestParam(value = "protein", required = false) Integer protein,
                       @RequestParam(value = "fat", required = false) Integer fat,
                       @RequestParam(value = "carbs", required = false) Integer carbs,
                       @RequestParam(value = "mealPlanId", required = false) Long mealPlanId) {

        LocalDate date = (dateStr != null && !dateStr.isBlank())
                ? LocalDate.parse(dateStr)
                : LocalDate.now();

        String typeStr = (type != null && !type.isBlank())
                ? type
                : MealType.BREAKFAST.name();

        MealType mealType = MealType.valueOf(typeStr);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByEmail(email).orElseThrow();

        Meal m = new Meal();
        m.setUser(user);
        m.setDate(date);
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
        
        return "redirect:/meal-plans/day?date=" + date;
    }
}


