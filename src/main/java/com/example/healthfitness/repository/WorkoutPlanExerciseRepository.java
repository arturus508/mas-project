package com.example.healthfitness.repository;

import com.example.healthfitness.model.WorkoutPlanExercise;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    java.util.List<WorkoutPlanExercise> findByWorkoutPlan_User_UserId(Long userId);

    @EntityGraph(attributePaths = "exercise")
    java.util.List<WorkoutPlanExercise> findWithExerciseByWorkoutPlan_WorkoutPlanId(Long workoutPlanId);

    @EntityGraph(attributePaths = "exercise")
    java.util.List<WorkoutPlanExercise> findWithExerciseByWorkoutPlan_User_UserId(Long userId);

    @Modifying
    @Query("update WorkoutPlanExercise e set e.exercise = null where e.exercise.exerciseId = :exerciseId")
    int clearExercise(@Param("exerciseId") Long exerciseId);
}
