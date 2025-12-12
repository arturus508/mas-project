package com.example.healthfitness.repository;

import com.example.healthfitness.model.WorkoutPlanExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutPlanExerciseRepository extends JpaRepository<WorkoutPlanExercise, Long> {
    List<WorkoutPlanExercise> findByWorkoutPlan_WorkoutPlanId(Long workoutPlanId);
}