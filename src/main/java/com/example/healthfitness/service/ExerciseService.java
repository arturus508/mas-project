package com.example.healthfitness.service;

import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.repository.DailyWorkoutSetRepository;
import com.example.healthfitness.repository.ExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanDayExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private DailyWorkoutSetRepository dailyWorkoutSetRepository;
    @Autowired
    private WorkoutPlanDayExerciseRepository workoutPlanDayExerciseRepository;
    @Autowired
    private WorkoutPlanExerciseRepository workoutPlanExerciseRepository;

    public Exercise saveExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + id));
    }

    @Transactional
    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + id));
        dailyWorkoutSetRepository.clearExercise(id);
        workoutPlanDayExerciseRepository.clearExercise(id);
        workoutPlanExerciseRepository.clearExercise(id);
        exerciseRepository.delete(exercise);
    }
}


