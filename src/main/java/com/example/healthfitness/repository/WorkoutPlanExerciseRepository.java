package com.example.healthfitness.repository;

import com.example.healthfitness.model.WorkoutPlanExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutPlanExerciseRepository extends JpaRepository<WorkoutPlanExercise, Long> {
    /**
     * Find all WorkoutPlanExercise entries for a given workout plan by id.
     * This allows fetching the exercises without triggering lazy loading on the WorkoutPlan entity.
     *
     * @param workoutPlanId the id of the workout plan
     * @return list of exercises belonging to the specified workout plan
     */
    java.util.List<WorkoutPlanExercise> findByWorkoutPlan_WorkoutPlanId(Long workoutPlanId);
}
