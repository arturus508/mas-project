package com.example.healthfitness.repository;

import com.example.healthfitness.model.WorkoutPlanDayExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkoutPlanDayExerciseRepository extends JpaRepository<WorkoutPlanDayExercise, Long> {
    List<WorkoutPlanDayExercise> findByPlanDay_PlanDayId(Long planDayId);

    @Modifying
    @Query("update WorkoutPlanDayExercise e set e.exercise = null where e.exercise.exerciseId = :exerciseId")
    int clearExercise(@Param("exerciseId") Long exerciseId);
}
