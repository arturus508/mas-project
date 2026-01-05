package com.example.healthfitness.repository;

import com.example.healthfitness.model.WorkoutPlanDay;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutPlanDayRepository extends JpaRepository<WorkoutPlanDay, Long> {
    List<WorkoutPlanDay> findByWorkoutPlan_WorkoutPlanIdOrderByDayOrderAsc(Long workoutPlanId);

    @EntityGraph(attributePaths = {"exercises", "exercises.exercise"})
    List<WorkoutPlanDay> findWithExercisesByWorkoutPlan_WorkoutPlanIdOrderByDayOrderAsc(Long workoutPlanId);

    @EntityGraph(attributePaths = {"workoutPlan"})
    List<WorkoutPlanDay> findWithPlanByWorkoutPlan_User_UserIdOrderByWorkoutPlan_WorkoutPlanIdAscDayOrderAsc(Long userId);

    @EntityGraph(attributePaths = {"exercises", "exercises.exercise", "workoutPlan", "workoutPlan.user"})
    java.util.Optional<WorkoutPlanDay> findWithExercisesByPlanDayId(Long planDayId);
}
