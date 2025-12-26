package com.example.healthfitness.controller;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.FoodItemRepository;
import com.example.healthfitness.service.MealMacroService;
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
@RequestMapping("/meals")
public class MealDayViewController {

    private final MealMacroService mealMacroService;
    private final FoodItemRepository foodItemRepository;
    private final CurrentUserService currentUserService;

    public MealDayViewController(MealMacroService mealMacroService,
                                 FoodItemRepository foodItemRepository,
                                 CurrentUserService currentUserService) {
        this.mealMacroService = mealMacroService;
        this.foodItemRepository = foodItemRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/today")
    public String today() {
        return "redirect:/meals?date=" + LocalDate.now();
    }

    @GetMapping
    public String day(@RequestParam(value = "date", required = false)
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
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
        model.addAttribute("itemsB", mealMacroService.getItems(userId, breakfast.getMealId()));
        model.addAttribute("itemsL", mealMacroService.getItems(userId, lunch.getMealId()));
        model.addAttribute("itemsD", mealMacroService.getItems(userId, dinner.getMealId()));
        model.addAttribute("itemsS", mealMacroService.getItems(userId, snack.getMealId()));
        return "meals-day";
    }

    @GetMapping("/{date}/{segment}")
    public String segment(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @PathVariable String segment,
                          @RequestParam(value = "q", required = false) String q,
                          Model model) {
        Long userId = currentUserService.id();
        MealType type = mealTypeFromSegment(segment);
        Meal meal = mealMacroService.getOrCreateMeal(userId, date, type);
        model.addAttribute("date", date);
        model.addAttribute("segment", segment);
        model.addAttribute("meal", meal);
        model.addAttribute("items", mealMacroService.getItems(userId, meal.getMealId()));
        model.addAttribute("totals", mealMacroService.totalsForMeal(meal.getMealId()));
        List<FoodItem> foods = q != null && !q.isBlank()
                ? foodItemRepository.findTop20ByNameContainingIgnoreCaseOrderByNameAsc(q)
                : foodItemRepository.findTop20ByNameContainingIgnoreCaseOrderByNameAsc("");
        model.addAttribute("foods", foods);
        model.addAttribute("q", q);
        return "meal-details";
    }

    @PostMapping("/{date}/{segment}/items/add-food")
    public String addFoodItem(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @PathVariable String segment,
                              @RequestParam Long foodItemId,
                              @RequestParam Integer quantity) {
        Long userId = currentUserService.id();
        MealType type = mealTypeFromSegment(segment);
        Meal meal = mealMacroService.getOrCreateMeal(userId, date, type);
        mealMacroService.addItem(userId, meal.getMealId(), foodItemId, quantity);
        return "redirect:/meals/" + date + "/" + segment;
    }

    @PostMapping("/{date}/{segment}/items/add-custom")
    public String addCustomItem(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                @PathVariable String segment,
                                @RequestParam String name,
                                @RequestParam Integer kcal,
                                @RequestParam Integer protein,
                                @RequestParam Integer fat,
                                @RequestParam Integer carbs,
                                @RequestParam(required = false) Integer quantity) {
        Long userId = currentUserService.id();
        MealType type = mealTypeFromSegment(segment);
        Meal meal = mealMacroService.getOrCreateMeal(userId, date, type);
        mealMacroService.addCustomItem(userId, meal.getMealId(), name, kcal, protein, fat, carbs, quantity);
        return "redirect:/meals/" + date + "/" + segment;
    }

    @PostMapping("/{date}/{segment}/items/{itemId}/delete")
    public String removeItem(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             @PathVariable String segment,
                             @PathVariable Long itemId) {
        Long userId = currentUserService.id();
        mealMacroService.removeItem(userId, itemId);
        return "redirect:/meals/" + date + "/" + segment;
    }

    @PostMapping("/copy-yesterday")
    public String copyYesterday(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = currentUserService.id();
        mealMacroService.copyDay(userId, date.minusDays(1), date);
        return "redirect:/meals?date=" + date;
    }

    private MealType mealTypeFromSegment(String segment) {
        if (segment == null) {
            throw new IllegalArgumentException("Segment is required");
        }
        String key = segment.trim().toUpperCase(Locale.ROOT);
        if (key.endsWith("S")) {
            key = key.substring(0, key.length() - 1);
        }
        return MealType.valueOf(key);
    }
}
