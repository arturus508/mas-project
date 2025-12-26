package com.example.healthfitness.repository;

import com.example.healthfitness.model.User;
import com.example.healthfitness.model.WorkoutPlan;
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
class WorkoutPlanRepositoryTest {

    @Autowired private WorkoutPlanRepository workoutPlanRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void findByUserIdReturnsWorkoutPlans() {
        User user = new User();
        user.setName("Workout User");
        user.setEmail("workout@local.test");
        user.setPassword("secret");
        user = userRepository.save(user);

        WorkoutPlan plan = new WorkoutPlan();
        plan.setPlanName("Starter");
        plan.setStatus("active");
        plan.setStartDate(LocalDate.now());
        plan.setEndDate(LocalDate.now().plusWeeks(1));
        plan.setDaysPerWeek(3);
        plan.setUser(user);
        workoutPlanRepository.save(plan);

        var results = workoutPlanRepository.findByUser_UserId(user.getUserId());
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPlanName()).isEqualTo("Starter");
    }
}
