package com.example.healthfitness.repository;

import com.example.healthfitness.model.WorkoutPlanExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanExerciseRepository extends JpaRepository<WorkoutPlanExercise, Long> {
}
