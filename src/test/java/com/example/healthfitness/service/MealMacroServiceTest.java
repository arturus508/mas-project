package com.example.healthfitness.service;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.FoodItemRepository;
import com.example.healthfitness.repository.MealItemRepository;
import com.example.healthfitness.repository.MealRepository;
import com.example.healthfitness.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MealMacroServiceTest {

    @Autowired private MealMacroService mealMacroService;
    @Autowired private UserRepository userRepository;
    @Autowired private MealRepository mealRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private MealItemRepository mealItemRepository;

    @Test
    void copyDayCopiesManualMacrosAndItems() {
        User user = new User();
        user.setName("Macro User");
        user.setEmail("macro@local.test");
        user.setPassword("secret");
        user = userRepository.save(user);

        FoodItem food = new FoodItem();
        food.setName("Test Food");
        food.setUnit(Unit.G);
        food.setKcal100(100);
        food.setProtein100(10);
        food.setFat100(5);
        food.setCarbs100(20);
        food = foodItemRepository.save(food);

        LocalDate from = LocalDate.now().minusDays(1);
        Meal src = new Meal();
        src.setUser(user);
        src.setMealType(MealType.BREAKFAST);
        src.setDate(from);
        src.setKcalManual(500);
        src.setProteinManual(40);
        src.setFatManual(20);
        src.setCarbsManual(50);
        src = mealRepository.save(src);

        MealItem item = new MealItem();
        item.setMeal(src);
        item.setFoodItem(food);
        item.setQuantity(100);
        src.getItems().add(item);
        mealItemRepository.save(item);

        LocalDate to = LocalDate.now();
        mealMacroService.copyDay(user.getUserId(), from, to);

        Meal copied = mealRepository.findByUserAndDateAndMealType(user, to, MealType.BREAKFAST);
        assertThat(copied).isNotNull();
        assertThat(copied.getKcalManual()).isEqualTo(500);
        assertThat(copied.getProteinManual()).isEqualTo(40);
        assertThat(copied.getFatManual()).isEqualTo(20);
        assertThat(copied.getCarbsManual()).isEqualTo(50);
        assertThat(copied.getItems()).hasSize(1);
        assertThat(copied.getItems().get(0).getFoodItem().getId()).isEqualTo(food.getId());
    }
}
