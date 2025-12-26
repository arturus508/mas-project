package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.repository.WorkoutPlanRepository;
import com.example.healthfitness.repository.WorkoutPlanExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkoutPlanExerciseService {

    @Autowired
    private WorkoutPlanExerciseRepository workoutPlanExerciseRepository;
    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    public WorkoutPlanExercise saveWorkoutPlanExercise(WorkoutPlanExercise workoutPlanExercise) {
        return workoutPlanExerciseRepository.save(workoutPlanExercise);
    }

    public WorkoutPlanExercise saveWorkoutPlanExercise(Long userId, WorkoutPlanExercise workoutPlanExercise) {
        if (workoutPlanExercise.getWorkoutPlan() == null || workoutPlanExercise.getWorkoutPlan().getWorkoutPlanId() == null) {
            throw new ResourceNotFoundException("Workout plan not found");
        }
        Long planId = workoutPlanExercise.getWorkoutPlan().getWorkoutPlanId();
        var plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with ID: " + planId));
        if (!plan.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan does not belong to current user");
        }
        workoutPlanExercise.setWorkoutPlan(plan);
        return workoutPlanExerciseRepository.save(workoutPlanExercise);
    }

    public List<WorkoutPlanExercise> getAllWorkoutPlanExercises() {
        return workoutPlanExerciseRepository.findAll();
    }

    public List<WorkoutPlanExercise> getWorkoutPlanExercisesForUser(Long userId) {
        return workoutPlanExerciseRepository.findWithExerciseByWorkoutPlan_User_UserId(userId);
    }

    public WorkoutPlanExercise getWorkoutPlanExerciseById(Long id) {
        return workoutPlanExerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlanExercise not found with id: " + id));
    }

    public WorkoutPlanExercise getWorkoutPlanExerciseById(Long userId, Long id) {
        WorkoutPlanExercise exercise = workoutPlanExerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlanExercise not found with id: " + id));
        if (!exercise.getWorkoutPlan().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan exercise does not belong to current user");
        }
        return exercise;
    }

    public void deleteWorkoutPlanExercise(Long id) {
        WorkoutPlanExercise exercise = workoutPlanExerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlanExercise not found with id: " + id));
        workoutPlanExerciseRepository.delete(exercise);
    }

    public void deleteWorkoutPlanExercise(Long userId, Long id) {
        WorkoutPlanExercise exercise = workoutPlanExerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlanExercise not found with id: " + id));
        if (!exercise.getWorkoutPlan().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan exercise does not belong to current user");
        }
        workoutPlanExerciseRepository.delete(exercise);
    }
}
