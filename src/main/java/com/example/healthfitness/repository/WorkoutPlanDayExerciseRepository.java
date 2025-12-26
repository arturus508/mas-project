package com.example.healthfitness.repository;

import com.example.healthfitness.model.WorkoutPlanDayExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutPlanDayExerciseRepository extends JpaRepository<WorkoutPlanDayExercise, Long> {
    List<WorkoutPlanDayExercise> findByPlanDay_PlanDayId(Long planDayId);
}
