package com.example.healthfitness.controller;

import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.service.WorkoutPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/workout-plans")
public class WorkoutPlanViewController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanViewController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping
    public String showWorkoutPlans(Model model) {
        model.addAttribute("workoutPlans", workoutPlanService.getAllWorkoutPlans());
        return "workout-plans"; // Refers to workout-plans.html
    }

    @GetMapping("/add")
    public String addWorkoutPlanPage(Model model) {
        model.addAttribute("workoutPlan", new WorkoutPlan());
        return "add-workout-plan";
    }

    @PostMapping("/add")
    public String addWorkoutPlan(@ModelAttribute WorkoutPlan workoutPlan) {
        workoutPlanService.saveWorkoutPlan(workoutPlan);
        return "redirect:/workout-plans";
    }

    @GetMapping("/edit/{id}")
    public String editWorkoutPlanPage(@PathVariable Long id, Model model) {
        model.addAttribute("workoutPlan", workoutPlanService.getWorkoutPlanById(id));
        return "edit-workout-plan"; // Refers to edit-workout-plan.html
    }

    @PostMapping("/edit/{id}")
    public String editWorkoutPlan(@PathVariable Long id, @ModelAttribute WorkoutPlan updatedWorkoutPlan) {
        workoutPlanService.updateWorkoutPlan(id, updatedWorkoutPlan);
        return "redirect:/workout-plans";
    }

    @GetMapping("/delete/{id}")
    public String deleteWorkoutPlan(@PathVariable Long id) {
        workoutPlanService.deleteWorkoutPlan(id);
        return "redirect:/workout-plans";
    }


}




