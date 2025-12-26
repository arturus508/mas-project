package com.example.healthfitness.controller;

import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.WorkoutPlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-plans")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;
    private final CurrentUserService currentUserService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService,
                                 CurrentUserService currentUserService) {
        this.workoutPlanService = workoutPlanService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<WorkoutPlan> getAllWorkoutPlans() {
        Long userId = currentUserService.id();
        return workoutPlanService.getWorkoutPlansByUser(userId);
    }

    @PostMapping("/{workoutPlanId}/exercises")
    public WorkoutPlanExercise addExerciseToWorkoutPlan(
            @PathVariable Long workoutPlanId,
            @RequestBody WorkoutPlanExercise workoutPlanExercise) {
        Long userId = currentUserService.id();
        return workoutPlanService.addExerciseToWorkoutPlan(userId, workoutPlanId, workoutPlanExercise);
    }

    @GetMapping("/{workoutPlanId}/exercises")
    public List<WorkoutPlanExercise> getExercisesByWorkoutPlan(@PathVariable Long workoutPlanId) {
        Long userId = currentUserService.id();
        return workoutPlanService.getExercisesByWorkoutPlan(userId, workoutPlanId);
    }
}








