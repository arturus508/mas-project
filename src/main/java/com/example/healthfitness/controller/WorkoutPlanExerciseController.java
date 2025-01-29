package com.example.healthfitness.controller;

import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.service.WorkoutPlanExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workoutplanexercises")
public class WorkoutPlanExerciseController {

    @Autowired
    private WorkoutPlanExerciseService workoutPlanExerciseService;

    @GetMapping
    public List<WorkoutPlanExercise> getAllWorkoutPlanExercises() {
        return workoutPlanExerciseService.getAllWorkoutPlanExercises();
    }

    @PostMapping
    public WorkoutPlanExercise createWorkoutPlanExercise(@RequestBody WorkoutPlanExercise workoutPlanExercise) {
        return workoutPlanExerciseService.saveWorkoutPlanExercise(workoutPlanExercise);
    }
}
