package com.example.healthfitness.controller;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.FoodItemRepository;
import com.example.healthfitness.service.MealMacroService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.CurrentUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Controller for displaying and editing the macro breakdown of meals on a
 * given day.  This refactoring replaces usage of
 * {@code SecurityContextHolder} with {@link CurrentUserService} to obtain
 * the current user's id.  Dates are bound to {@link LocalDate} via
 * {@link DateTimeFormat} for type safety.
 */
@Controller
@RequestMapping("/meal-plans/day")
public class MealDayViewController {

    private final MealMacroService mealMacroService;
    private final FoodItemRepository foodItemRepository;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public MealDayViewController(MealMacroService mealMacroService,
                                 FoodItemRepository foodItemRepository,
                                 UserService userService,
                                 CurrentUserService currentUserService) {
        this.mealMacroService = mealMacroService;
        this.foodItemRepository = foodItemRepository;
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String day(@RequestParam(value = "date", required = false)
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      @RequestParam(value = "q", required = false) String q,
                      Model model) {
        Long userId = currentUserService.id();
        var d = date != null ? date : LocalDate.now();
        var breakfast = mealMacroService.getOrCreateMeal(userId, d, MealType.BREAKFAST);
        var lunch     = mealMacroService.getOrCreateMeal(userId, d, MealType.LUNCH);
        var dinner    = mealMacroService.getOrCreateMeal(userId, d, MealType.DINNER);
        var snack     = mealMacroService.getOrCreateMeal(userId, d, MealType.SNACK);
        model.addAttribute("date", d);
        model.addAttribute("breakfast", breakfast);
        model.addAttribute("lunch", lunch);
        model.addAttribute("dinner", dinner);
        model.addAttribute("snack", snack);
        model.addAttribute("totB", mealMacroService.totalsForMeal(breakfast.getMealId()));
        model.addAttribute("totL", mealMacroService.totalsForMeal(lunch.getMealId()));
        model.addAttribute("totD", mealMacroService.totalsForMeal(dinner.getMealId()));
        model.addAttribute("totS", mealMacroService.totalsForMeal(snack.getMealId()));
        model.addAttribute("totDay", mealMacroService.totalsForDay(userId, d));
        List<FoodItem> foods = q != null && !q.isBlank()
                ? foodItemRepository.findTop20ByNameContainingIgnoreCaseOrderByNameAsc(q)
                : foodItemRepository.findTop20ByNameContainingIgnoreCaseOrderByNameAsc("");
        model.addAttribute("foods", foods);
        model.addAttribute("q", q);
        return "meals-day";
    }

    @PostMapping("/{mealId}/add-item")
    public String addItem(@PathVariable Long mealId,
                          @RequestParam Long foodItemId,
                          @RequestParam Integer quantity,
                          @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        mealMacroService.addItem(mealId, foodItemId, quantity);
        return "redirect:/meal-plans/day?date=" + date;
    }

    @PostMapping("/remove-item/{itemId}")
    public String removeItem(@PathVariable Long itemId,
                             @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        mealMacroService.removeItem(itemId);
        return "redirect:/meal-plans/day?date=" + date;
    }

    @PostMapping("/{mealId}/quick")
    public String quick(@PathVariable Long mealId,
                        @RequestParam Integer kcal,
                        @RequestParam Integer protein,
                        @RequestParam Integer fat,
                        @RequestParam Integer carbs,
                        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        mealMacroService.setManual(mealId, kcal, protein, fat, carbs);
        return "redirect:/meal-plans/day?date=" + date;
    }

    @PostMapping("/copy-yesterday")
    public String copyYesterday(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = currentUserService.id();
        mealMacroService.copyDay(userId, date.minusDays(1), date);
        return "redirect:/meal-plans/day?date=" + date;
    }
}