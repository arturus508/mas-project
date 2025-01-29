package com.example.healthfitness.service;

import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.repository.WorkoutPlanExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutPlanService {

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private WorkoutPlanExerciseRepository workoutPlanExerciseRepository;

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }

    public WorkoutPlan getWorkoutPlanById(Long workoutPlanId) {
        return workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new RuntimeException("Workout plan not found with ID: " + workoutPlanId));
    }

    public WorkoutPlan saveWorkoutPlan(WorkoutPlan workoutPlan) {
        return workoutPlanRepository.save(workoutPlan);
    }

    public void deleteWorkoutPlan(Long workoutPlanId) {
        workoutPlanRepository.deleteById(workoutPlanId);
    }

    public WorkoutPlanExercise addExerciseToWorkoutPlan(Long workoutPlanId, WorkoutPlanExercise workoutPlanExercise) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new RuntimeException("Workout plan not found with ID: " + workoutPlanId));

        workoutPlanExercise.setWorkoutPlan(workoutPlan);
        return workoutPlanExerciseRepository.save(workoutPlanExercise);
    }

    public List<WorkoutPlanExercise> getExercisesByWorkoutPlan(Long workoutPlanId) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new RuntimeException("Workout plan not found with ID: " + workoutPlanId));

        return workoutPlan.getWorkoutPlanExercises();
    }

    public WorkoutPlan updateWorkoutPlan(Long workoutPlanId, WorkoutPlan updatedWorkoutPlan) {
        return workoutPlanRepository.findById(workoutPlanId).map(existingWorkoutPlan -> {
            existingWorkoutPlan.setPlanName(updatedWorkoutPlan.getPlanName());
            existingWorkoutPlan.setDescription(updatedWorkoutPlan.getDescription());
            existingWorkoutPlan.setStartDate(updatedWorkoutPlan.getStartDate());
            existingWorkoutPlan.setEndDate(updatedWorkoutPlan.getEndDate());
            existingWorkoutPlan.setDaysPerWeek(updatedWorkoutPlan.getDaysPerWeek());
            return workoutPlanRepository.save(existingWorkoutPlan);
        }).orElseThrow(() -> new RuntimeException("Workout plan not found with ID: " + workoutPlanId));
    }
}




