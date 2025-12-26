package com.example.healthfitness.repository;

import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.model.MealType;
import com.example.healthfitness.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MealRepositoryTest {

    @Autowired private MealRepository mealRepository;
    @Autowired private MealPlanRepository mealPlanRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void mealQueriesReturnExpectedData() {
        User user = new User();
        user.setName("Meal User");
        user.setEmail("meal@local.test");
        user.setPassword("secret");
        user = userRepository.save(user);

        MealPlan plan = new MealPlan();
        plan.setPlanName("Meal Plan");
        plan.setStartDate(LocalDate.now());
        plan.setEndDate(LocalDate.now().plusDays(1));
        plan.setUser(user);
        plan = mealPlanRepository.save(plan);

        Meal meal = new Meal();
        meal.setUser(user);
        meal.setMealPlan(plan);
        meal.setMealType(MealType.BREAKFAST);
        meal.setDate(LocalDate.now());
        meal.setTotalCalories(0);
        mealRepository.save(meal);

        Meal fetched = mealRepository.findByUserAndDateAndMealType(user, LocalDate.now(), MealType.BREAKFAST);
        assertThat(fetched).isNotNull();
        assertThat(fetched.getMealPlan().getMealPlanId()).isEqualTo(plan.getMealPlanId());

        var byPlan = mealRepository.findByMealPlan_MealPlanId(plan.getMealPlanId());
        assertThat(byPlan).hasSize(1);
    }
}
