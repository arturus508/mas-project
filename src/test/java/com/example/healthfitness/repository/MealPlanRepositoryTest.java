package com.example.healthfitness.repository;

import com.example.healthfitness.model.MealPlan;
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
class MealPlanRepositoryTest {

    @Autowired private MealPlanRepository mealPlanRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void findByUserIdReturnsPlans() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@local.test");
        user.setPassword("secret");
        user = userRepository.save(user);

        MealPlan plan = new MealPlan();
        plan.setPlanName("Plan A");
        plan.setDescription("Test plan");
        plan.setDietaryRestriction("None");
        plan.setStartDate(LocalDate.now());
        plan.setEndDate(LocalDate.now().plusDays(7));
        plan.setUser(user);
        mealPlanRepository.save(plan);

        var results = mealPlanRepository.findByUser_UserId(user.getUserId());

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPlanName()).isEqualTo("Plan A");
    }
}
