package com.example.healthfitness.controller;

import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealType;
import com.example.healthfitness.repository.FoodItemRepository;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.MealMacroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MealDayViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MealDayViewControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private MealMacroService mealMacroService;
    @MockBean private FoodItemRepository foodItemRepository;
    @MockBean private CurrentUserService currentUserService;

    @Test
    void getDayRendersPageAndTotals() throws Exception {
        LocalDate date = LocalDate.of(2025, 1, 2);
        Meal breakfast = mealWithId(1L, MealType.BREAKFAST);
        Meal lunch = mealWithId(2L, MealType.LUNCH);
        Meal dinner = mealWithId(3L, MealType.DINNER);
        Meal snack = mealWithId(4L, MealType.SNACK);

        when(currentUserService.id()).thenReturn(1L);
        when(mealMacroService.getOrCreateMeal(1L, date, MealType.BREAKFAST)).thenReturn(breakfast);
        when(mealMacroService.getOrCreateMeal(1L, date, MealType.LUNCH)).thenReturn(lunch);
        when(mealMacroService.getOrCreateMeal(1L, date, MealType.DINNER)).thenReturn(dinner);
        when(mealMacroService.getOrCreateMeal(1L, date, MealType.SNACK)).thenReturn(snack);
        when(mealMacroService.totalsForMeal(1L)).thenReturn(Map.of("kcal", 100, "protein", 10, "fat", 5, "carbs", 15));
        when(mealMacroService.totalsForMeal(2L)).thenReturn(Map.of("kcal", 200, "protein", 20, "fat", 10, "carbs", 30));
        when(mealMacroService.totalsForMeal(3L)).thenReturn(Map.of("kcal", 300, "protein", 30, "fat", 15, "carbs", 45));
        when(mealMacroService.totalsForMeal(4L)).thenReturn(Map.of("kcal", 80, "protein", 8, "fat", 4, "carbs", 12));
        when(mealMacroService.totalsForDay(1L, date)).thenReturn(Map.of("kcal", 680, "protein", 68, "fat", 34, "carbs", 102));
        when(mealMacroService.getItems(1L, 1L)).thenReturn(List.of());
        when(mealMacroService.getItems(1L, 2L)).thenReturn(List.of());
        when(mealMacroService.getItems(1L, 3L)).thenReturn(List.of());
        when(mealMacroService.getItems(1L, 4L)).thenReturn(List.of());

        mockMvc.perform(get("/meals")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals-day"))
                .andExpect(model().attributeExists("date", "breakfast", "lunch", "dinner", "snack"))
                .andExpect(model().attributeExists("totB", "totL", "totD", "totS", "totDay"))
                .andExpect(model().attributeExists("itemsB", "itemsL", "itemsD", "itemsS"));
    }

    @Test
    void addItemRedirects() throws Exception {
        LocalDate date = LocalDate.of(2025, 1, 2);
        when(currentUserService.id()).thenReturn(1L);
        when(mealMacroService.getOrCreateMeal(1L, date, MealType.BREAKFAST))
                .thenReturn(mealWithId(1L, MealType.BREAKFAST));

        mockMvc.perform(post("/meals/" + date + "/breakfast/items/add-food")
                        .param("foodItemId", "5")
                        .param("quantity", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/meals/" + date + "/breakfast?saved=1"));

        verify(mealMacroService).addItem(1L, 1L, 5L, 100);
    }

    @Test
    void copyYesterdayRedirects() throws Exception {
        LocalDate date = LocalDate.of(2025, 1, 2);
        when(currentUserService.id()).thenReturn(1L);

        mockMvc.perform(post("/meals/copy-yesterday")
                        .param("date", date.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/meals?date=" + date + "&copied=1"));

        verify(mealMacroService).copyDay(1L, date.minusDays(1), date);
    }

    private Meal mealWithId(Long id, MealType type) {
        Meal meal = new Meal();
        meal.setMealId(id);
        meal.setMealType(type);
        meal.setDate(LocalDate.of(2025, 1, 2));
        return meal;
    }

}
