package com.example.healthfitness.controller;

import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.WorkoutPlanExerciseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workoutplanexercises")
public class WorkoutPlanExerciseController {

    private final WorkoutPlanExerciseService workoutPlanExerciseService;
    private final CurrentUserService currentUserService;

    public WorkoutPlanExerciseController(WorkoutPlanExerciseService workoutPlanExerciseService,
                                         CurrentUserService currentUserService) {
        this.workoutPlanExerciseService = workoutPlanExerciseService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<WorkoutPlanExercise> getAllWorkoutPlanExercises() {
        Long userId = currentUserService.id();
        return workoutPlanExerciseService.getWorkoutPlanExercisesForUser(userId);
    }

    @PostMapping
    public WorkoutPlanExercise createWorkoutPlanExercise(@RequestBody WorkoutPlanExercise workoutPlanExercise) {
        Long userId = currentUserService.id();
        return workoutPlanExerciseService.saveWorkoutPlanExercise(userId, workoutPlanExercise);
    }
}
