package com.example.healthfitness.service;

import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.repository.WorkoutPlanExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkoutPlanService {

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private WorkoutPlanExerciseRepository workoutPlanExerciseRepository;

    // Get all workout plans
    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }

    // Get all workout plans for a specific user.
    public List<WorkoutPlan> getWorkoutPlansByUser(Long userId) {
        return workoutPlanRepository.findByUser_UserId(userId);
    }

    // Get one workout plan by its ID.
    public WorkoutPlan getWorkoutPlanById(Long workoutPlanId) {
        return workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new RuntimeException("Workout plan not found with ID: " + workoutPlanId));
    }

    // Save a new workout plan.
    public WorkoutPlan saveWorkoutPlan(WorkoutPlan workoutPlan) {
        return workoutPlanRepository.save(workoutPlan);
    }

    // Delete a workout plan.
    public void deleteWorkoutPlan(Long workoutPlanId) {
        workoutPlanRepository.deleteById(workoutPlanId);
    }

    // Update an existing workout plan.
    public WorkoutPlan updateWorkoutPlan(Long workoutPlanId, WorkoutPlan updatedWorkoutPlan) {
        return workoutPlanRepository.findById(workoutPlanId).map(existingPlan -> {
            existingPlan.setPlanName(updatedWorkoutPlan.getPlanName());
            existingPlan.setDescription(updatedWorkoutPlan.getDescription());
            existingPlan.setStatus(updatedWorkoutPlan.getStatus());
            existingPlan.setStartDate(updatedWorkoutPlan.getStartDate());
            existingPlan.setEndDate(updatedWorkoutPlan.getEndDate());
            existingPlan.setDaysPerWeek(updatedWorkoutPlan.getDaysPerWeek());
            return workoutPlanRepository.save(existingPlan);
        }).orElseThrow(() -> new RuntimeException("Workout plan not found with ID: " + workoutPlanId));
    }

    // Add an exercise to a workout plan.
    public WorkoutPlanExercise addExerciseToWorkoutPlan(Long workoutPlanId, WorkoutPlanExercise workoutPlanExercise) {
        WorkoutPlan workoutPlan = getWorkoutPlanById(workoutPlanId);
        workoutPlanExercise.setWorkoutPlan(workoutPlan);
        return workoutPlanExerciseRepository.save(workoutPlanExercise);
    }

    // Get the list of exercises attached to a workout plan.
    public List<WorkoutPlanExercise> getExercisesByWorkoutPlan(Long workoutPlanId) {
        WorkoutPlan workoutPlan = getWorkoutPlanById(workoutPlanId);
        return workoutPlan.getWorkoutPlanExercises();
    }
}






