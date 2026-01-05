package com.example.healthfitness.integration;

import com.example.healthfitness.model.FoodItem;
import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealItem;
import com.example.healthfitness.model.MealType;
import com.example.healthfitness.model.Unit;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.FoodItemRepository;
import com.example.healthfitness.repository.MealItemRepository;
import com.example.healthfitness.repository.MealRepository;
import com.example.healthfitness.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class MealsFlowIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private MealRepository mealRepository;
    @Autowired private MealItemRepository mealItemRepository;

    private User user;
    private FoodItem foodItem;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        user = new User("Test User", "test@example.com", "secret");
        userRepository.save(user);
        foodItem = new FoodItem();
        foodItem.setName("Egg");
        foodItem.setUnit(Unit.G);
        foodItem.setKcal100(155);
        foodItem.setProtein100(13);
        foodItem.setFat100(11);
        foodItem.setCarbs100(1);
        foodItemRepository.save(foodItem);
        date = LocalDate.of(2026, 1, 2);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        "N/A",
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void mealItemsAndCopyFlow() throws Exception {
        mockMvc.perform(post("/meals/{date}/{segment}/items/add-food", date, "breakfast")
                        .param("foodItemId", foodItem.getId().toString())
                        .param("quantity", "100"))
                .andExpect(status().is3xxRedirection());

        Meal breakfast = mealRepository.findByUserAndDateAndMealType(user, date, MealType.BREAKFAST);
        assertThat(breakfast).isNotNull();
        List<MealItem> items = mealItemRepository.findByMeal_MealId(breakfast.getMealId());
        assertThat(items).hasSize(1);

        mockMvc.perform(post("/meals/{date}/{segment}/items/add-custom", date, "breakfast")
                        .param("name", "Custom oatmeal")
                        .param("kcal", "200")
                        .param("protein", "10")
                        .param("fat", "5")
                        .param("carbs", "30")
                        .param("quantity", "100"))
                .andExpect(status().is3xxRedirection());
        assertThat(foodItemRepository.findByNameIgnoreCase("Custom oatmeal")).isPresent();

        MealItem item = mealItemRepository.findByMeal_MealId(breakfast.getMealId()).get(0);
        mockMvc.perform(post("/meals/{date}/{segment}/items/{itemId}/delete", date, "breakfast", item.getId()))
                .andExpect(status().is3xxRedirection());
        assertThat(mealItemRepository.findByMeal_MealId(breakfast.getMealId()).size()).isLessThan(2);

        LocalDate target = date.plusDays(1);
        mockMvc.perform(post("/meals/copy-yesterday")
                        .param("date", target.toString()))
                .andExpect(status().is3xxRedirection());
        Meal copied = mealRepository.findByUserAndDateAndMealType(user, target, MealType.BREAKFAST);
        assertThat(copied).isNotNull();
    }
}
