package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.model.User;
import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.repository.ExerciseRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.repository.WorkoutPlanExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WorkoutPlanServiceTest {

    @Autowired private WorkoutPlanService workoutPlanService;
    @Autowired private WorkoutPlanExerciseRepository workoutPlanExerciseRepository;
    @Autowired private WorkoutPlanRepository workoutPlanRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void addExerciseToPlanStoresExercise() {
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
        plan = workoutPlanRepository.save(plan);

        Exercise exercise = new Exercise();
        exercise.setExerciseName("Push Up Test");
        exercise.setMuscleGroup("Chest");
        exercise.setIntensityLevel("Light");
        exercise = exerciseRepository.save(exercise);

        WorkoutPlanExercise wpe = new WorkoutPlanExercise();
        wpe.setExercise(exercise);
        wpe.setSets(3);
        wpe.setReps(10);
        wpe.setRestTime(60);

        workoutPlanService.addExerciseToWorkoutPlan(user.getUserId(), plan.getWorkoutPlanId(), wpe);

        var saved = workoutPlanExerciseRepository.findByWorkoutPlan_WorkoutPlanId(plan.getWorkoutPlanId());
        assertThat(saved).hasSize(1);
        assertThat(saved.get(0).getExercise().getExerciseName()).isEqualTo("Push Up Test");
    }

    @Test
    void addExerciseToOtherUsersPlanThrowsForbidden() {
        User user1 = new User();
        user1.setName("Workout User 1");
        user1.setEmail("workout1@local.test");
        user1.setPassword("secret");
        user1 = userRepository.save(user1);
        final Long user1Id = user1.getUserId();

        User user2 = new User();
        user2.setName("Workout User 2");
        user2.setEmail("workout2@local.test");
        user2.setPassword("secret");
        user2 = userRepository.save(user2);

        WorkoutPlan plan = new WorkoutPlan();
        plan.setPlanName("User2 Plan");
        plan.setStatus("active");
        plan.setStartDate(LocalDate.now());
        plan.setEndDate(LocalDate.now().plusWeeks(1));
        plan.setDaysPerWeek(3);
        plan.setUser(user2);
        plan = workoutPlanRepository.save(plan);

        Exercise exercise = new Exercise();
        exercise.setExerciseName("Squat Test");
        exercise.setMuscleGroup("Legs");
        exercise.setIntensityLevel("Medium");
        exercise = exerciseRepository.save(exercise);

        WorkoutPlanExercise wpe = new WorkoutPlanExercise();
        wpe.setExercise(exercise);
        wpe.setSets(3);
        wpe.setReps(8);
        wpe.setRestTime(90);

        WorkoutPlan savedPlan = plan;
        assertThatThrownBy(() ->
                workoutPlanService.addExerciseToWorkoutPlan(user1Id, savedPlan.getWorkoutPlanId(), wpe))
                .isInstanceOf(ForbiddenException.class);
    }
}
