package com.example.healthfitness.controller;

import com.example.healthfitness.model.DailyWorkout;
import com.example.healthfitness.model.DailyWorkoutSet;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.DailyWorkoutService;
import com.example.healthfitness.service.ExerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/workouts")
public class DailyWorkoutViewController {

    private final DailyWorkoutService dailyWorkoutService;
    private final CurrentUserService currentUserService;
    private final ExerciseService exerciseService;

    public DailyWorkoutViewController(DailyWorkoutService dailyWorkoutService,
                                      CurrentUserService currentUserService,
                                      ExerciseService exerciseService) {
        this.dailyWorkoutService = dailyWorkoutService;
        this.currentUserService = currentUserService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/today")
    public String showTodayWorkout(Model model) {
        return showWorkoutForDate(LocalDate.now(), model);
    }

    @GetMapping("/{date}")
    public String showWorkoutForDate(@PathVariable LocalDate date, Model model) {
        Long userId = currentUserService.id();
        DailyWorkout workout = dailyWorkoutService.findForDate(userId, date);
        model.addAttribute("dailyWorkout", workout);
        if (workout != null) {
            List<DailyWorkoutSet> sets = dailyWorkoutService.getSets(userId, workout.getDailyWorkoutId());
            model.addAttribute("sets", sets);
        }
        model.addAttribute("globalExercises", exerciseService.getAllExercises());
        return "daily-workout";
    }

    @PostMapping("/{dailyWorkoutId}/sets/add")
    public String addSet(@PathVariable Long dailyWorkoutId,
                         @RequestParam Long exerciseId) {
        Long userId = currentUserService.id();
        dailyWorkoutService.addSet(userId, dailyWorkoutId, exerciseId);
        return "redirect:/workouts/today";
    }

    @PostMapping("/sets/{setId}/reps")
    public String updateReps(@PathVariable Long setId,
                             @RequestParam(required = false) Integer repsDone) {
        Long userId = currentUserService.id();
        dailyWorkoutService.updateReps(userId, setId, repsDone);
        return "redirect:/workouts/today";
    }

    @PostMapping("/sets/{setId}/delete")
    public String deleteSet(@PathVariable Long setId) {
        Long userId = currentUserService.id();
        dailyWorkoutService.deleteSet(userId, setId);
        return "redirect:/workouts/today";
    }
}
