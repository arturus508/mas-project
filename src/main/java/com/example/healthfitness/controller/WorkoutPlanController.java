package com.example.healthfitness.controller;

import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.service.WorkoutPlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-plans")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping
    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanService.getAllWorkoutPlans();
    }

    @PostMapping("/{workoutPlanId}/exercises")
    public WorkoutPlanExercise addExerciseToWorkoutPlan(
            @PathVariable Long workoutPlanId,
            @RequestBody WorkoutPlanExercise workoutPlanExercise) {
        return workoutPlanService.addExerciseToWorkoutPlan(workoutPlanId, workoutPlanExercise);
    }

    @GetMapping("/{workoutPlanId}/exercises")
    public List<WorkoutPlanExercise> getExercisesByWorkoutPlan(@PathVariable Long workoutPlanId) {
        return workoutPlanService.getExercisesByWorkoutPlan(workoutPlanId);
    }
}








