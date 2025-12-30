package com.example.healthfitness.controller;

import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.service.ExerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    // Display the form to create a new Exercise.
    @GetMapping("/add")
    public String addExercisePage(@RequestParam(value = "returnTo", required = false) String returnTo,
                                  Model model) {
        model.addAttribute("exercise", new Exercise());
        model.addAttribute("returnTo", sanitizeReturnTo(returnTo));
        return "exercise-form"; // corresponds to exercise-form.html
    }

    // Process the form submission to create a new Exercise.
    @PostMapping("/add")
    public String addExercise(@ModelAttribute Exercise exercise,
                              @RequestParam(value = "returnTo", required = false) String returnTo) {
        exerciseService.saveExercise(exercise);
        String safeReturn = sanitizeReturnTo(returnTo);
        if (safeReturn != null) {
            return "redirect:" + safeReturn;
        }
        return "redirect:/exercises"; // Redirect to the global list view.
    }

    private String sanitizeReturnTo(String returnTo) {
        if (returnTo == null || returnTo.isBlank()) {
            return null;
        }
        String trimmed = returnTo.trim();
        if (!trimmed.startsWith("/") || trimmed.startsWith("//")) {
            return null;
        }
        return trimmed;
    }

    // Display the global list of exercises.
    @GetMapping
    public String listExercises(Model model) {
        model.addAttribute("exercises", exerciseService.getAllExercises());
        return "exercises"; // corresponds to exercises.html
    }
}




