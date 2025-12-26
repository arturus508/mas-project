package com.example.healthfitness.config;

import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.model.MealType;
import com.example.healthfitness.model.User;
import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.repository.ExerciseRepository;
import com.example.healthfitness.repository.MealPlanRepository;
import com.example.healthfitness.repository.MealRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.repository.WorkoutPlanExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
@Profile("dev")
public class DevDataSeedConfig {

    @Bean
    CommandLineRunner seedDevData(UserRepository userRepository,
                                  MealPlanRepository mealPlanRepository,
                                  MealRepository mealRepository,
                                  WorkoutPlanRepository workoutPlanRepository,
                                  WorkoutPlanExerciseRepository workoutPlanExerciseRepository,
                                  ExerciseRepository exerciseRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {
            User user = userRepository.findByEmail("dev@local.test")
                .orElseGet(() -> {
                    User u = new User();
                    u.setName("Dev User");
                    u.setEmail("dev@local.test");
                    u.setPassword(passwordEncoder.encode("devpass"));
                    return userRepository.save(u);
                });

            seedWorkoutPlan(user, workoutPlanRepository, workoutPlanExerciseRepository, exerciseRepository);
            seedMealPlan(user, mealPlanRepository, mealRepository);
        };
    }

    private void seedWorkoutPlan(User user,
                                 WorkoutPlanRepository workoutPlanRepository,
                                 WorkoutPlanExerciseRepository workoutPlanExerciseRepository,
                                 ExerciseRepository exerciseRepository) {
        List<WorkoutPlan> plans = workoutPlanRepository.findByUser_UserId(user.getUserId());
        boolean exists = plans.stream().anyMatch(p -> "Starter Plan".equalsIgnoreCase(p.getPlanName()));
        if (exists) {
            return;
        }

        WorkoutPlan plan = new WorkoutPlan();
        plan.setPlanName("Starter Plan");
        plan.setDescription("Simple 3-day split for demo data.");
        plan.setStatus("active");
        plan.setStartDate(LocalDate.now());
        plan.setEndDate(LocalDate.now().plusWeeks(4));
        plan.setDaysPerWeek(3);
        plan.setUser(user);
        WorkoutPlan savedPlan = workoutPlanRepository.save(plan);

        if (workoutPlanExerciseRepository.findByWorkoutPlan_WorkoutPlanId(savedPlan.getWorkoutPlanId()).isEmpty()) {
            exerciseRepository.findAll().stream().findFirst().ifPresent(ex -> {
                WorkoutPlanExercise wpe = new WorkoutPlanExercise();
                wpe.setWorkoutPlan(savedPlan);
                wpe.setExercise(ex);
                wpe.setSets(3);
                wpe.setReps(10);
                wpe.setRestTime(90);
                workoutPlanExerciseRepository.save(wpe);
            });
        }
    }

    private void seedMealPlan(User user,
                              MealPlanRepository mealPlanRepository,
                              MealRepository mealRepository) {
        List<MealPlan> plans = mealPlanRepository.findByUser_UserId(user.getUserId());
        boolean exists = plans.stream().anyMatch(p -> "Demo Meal Plan".equalsIgnoreCase(p.getPlanName()));
        if (exists) {
            return;
        }

        MealPlan plan = new MealPlan();
        plan.setPlanName("Demo Meal Plan");
        plan.setDescription("Basic meal plan for demo data.");
        plan.setDietaryRestriction("None");
        plan.setStartDate(LocalDate.now());
        plan.setEndDate(LocalDate.now().plusWeeks(1));
        plan.setUser(user);
        MealPlan savedPlan = mealPlanRepository.save(plan);

        LocalDate today = LocalDate.now();
        Meal existing = mealRepository.findByUserAndDateAndMealType(user, today, MealType.BREAKFAST);
        if (existing == null) {
            Meal meal = new Meal();
            meal.setUser(user);
            meal.setMealPlan(savedPlan);
            meal.setMealType(MealType.BREAKFAST);
            meal.setDate(today);
            meal.setProteinManual(30);
            meal.setFatManual(10);
            meal.setCarbsManual(40);
            mealRepository.save(meal);
        }
    }
}
