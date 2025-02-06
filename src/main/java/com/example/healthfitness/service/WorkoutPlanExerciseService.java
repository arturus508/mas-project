package com.example.healthfitness.service;

import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.repository.WorkoutPlanExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkoutPlanExerciseService {

    @Autowired
    private WorkoutPlanExerciseRepository workoutPlanExerciseRepository;

    public WorkoutPlanExercise saveWorkoutPlanExercise(WorkoutPlanExercise workoutPlanExercise) {
        return workoutPlanExerciseRepository.save(workoutPlanExercise);
    }

    public List<WorkoutPlanExercise> getAllWorkoutPlanExercises() {
        return workoutPlanExerciseRepository.findAll();
    }

    public WorkoutPlanExercise getWorkoutPlanExerciseById(Long id) {
        return workoutPlanExerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkoutPlanExercise not found"));
    }

    public void deleteWorkoutPlanExercise(Long id) {
        workoutPlanExerciseRepository.deleteById(id);
    }
}
