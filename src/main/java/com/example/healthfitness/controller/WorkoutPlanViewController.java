package com.example.healthfitness.controller;

import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.model.User;
import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.service.ExerciseService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.WorkoutPlanService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/workout-plans")
public class WorkoutPlanViewController {

    private final WorkoutPlanService workoutPlanService;
    private final ExerciseService exerciseService;
    private final UserService userService;

    public WorkoutPlanViewController(WorkoutPlanService workoutPlanService, ExerciseService exerciseService, UserService userService) {
        this.workoutPlanService = workoutPlanService;
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    // --------------------- Workout Plan CRUD ---------------------

    // List workout plans for the logged-in user.
    @GetMapping
    public String showWorkoutPlans(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        model.addAttribute("workoutPlans", workoutPlanService.getWorkoutPlansByUser(user.getUserId()));
        return "workout-plans"; // corresponds to workout-plans.html
    }

    // Display the Add Workout Plan page.
    @GetMapping("/add")
    public String addWorkoutPlanPage(Model model) {
        model.addAttribute("workoutPlan", new WorkoutPlan());
        return "add-workout-plan"; // corresponds to add-workout-plan.html
    }

    // Process the Add Workout Plan form submission.
    @PostMapping("/add")
    public String addWorkoutPlan(@ModelAttribute WorkoutPlan workoutPlan) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        workoutPlan.setUser(user);
        workoutPlanService.saveWorkoutPlan(workoutPlan);
        return "redirect:/workout-plans";
    }

    // Display the Edit Workout Plan page.
    @GetMapping("/edit/{id}")
    public String editWorkoutPlanPage(@PathVariable Long id, Model model) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        if (workoutPlan == null) {
            throw new RuntimeException("Workout Plan not found with id: " + id);
        }
        model.addAttribute("workoutPlan", workoutPlan);
        return "edit-workout-plan"; // corresponds to edit-workout-plan.html
    }

    // Process the Edit Workout Plan form submission.
    @PostMapping("/edit/{id}")
    public String editWorkoutPlan(@PathVariable Long id, @ModelAttribute WorkoutPlan updatedWorkoutPlan) {
        workoutPlanService.updateWorkoutPlan(id, updatedWorkoutPlan);
        return "redirect:/workout-plans";
    }

    // Delete a Workout Plan.
    @GetMapping("/delete/{id}")
    public String deleteWorkoutPlan(@PathVariable Long id) {
        workoutPlanService.deleteWorkoutPlan(id);
        return "redirect:/workout-plans";
    }

    // --------------------- Workout Plan Exercise Management ---------------------

    // Display the Workout Plan Details page (showing its exercises).
    @GetMapping("/{id}/exercises")
    public String getWorkoutPlanDetails(@PathVariable Long id, Model model) {
        WorkoutPlan plan = workoutPlanService.getWorkoutPlanById(id);
        if (plan == null) {
            throw new RuntimeException("Workout plan not found with id: " + id);
        }
        model.addAttribute("workoutPlan", plan);
        // Fetch exercises via service to avoid lazy initialization issues
        model.addAttribute("exercises", workoutPlanService.getExercisesByWorkoutPlan(id));
        model.addAttribute("workoutPlanId", id);
        return "workout-plan-details"; // corresponds to workout-plan-details.html
    }

    // Display the form to add a new exercise to a workout plan.
    @GetMapping("/{id}/exercises/add")
    public String addWorkoutPlanExercisePage(@PathVariable("id") Long workoutPlanId, Model model) {
        WorkoutPlanExercise wpe = new WorkoutPlanExercise();
        // Initialize the nested Exercise so that binding works in the form.
        wpe.setExercise(new Exercise());
        model.addAttribute("workoutPlanExercise", wpe);
        model.addAttribute("workoutPlanId", workoutPlanId);
        // Populate the dropdown with the global list of exercises.
        model.addAttribute("globalExercises", exerciseService.getAllExercises());
        return "workout-plan-exercise-form"; // corresponds to workout-plan-exercise-form.html
    }

    // Process the form submission to add a new exercise to the workout plan.
    // (After successful addition, we redirect back to the add exercise page so the user can add another exercise.)
    @PostMapping("/{id}/exercises/add")
    public String addWorkoutPlanExercise(@PathVariable("id") Long workoutPlanId,
                                         @ModelAttribute WorkoutPlanExercise workoutPlanExercise) {
        // The dropdown binds exercise.exerciseId from the selected option.
        Long exerciseId = workoutPlanExercise.getExercise().getExerciseId();
        Exercise exercise = exerciseService.getExerciseById(exerciseId);
        workoutPlanExercise.setExercise(exercise);
        workoutPlanService.addExerciseToWorkoutPlan(workoutPlanId, workoutPlanExercise);
        // Redirect back to the add exercise view instead of the details list.
        return "redirect:/workout-plans/" + workoutPlanId + "/exercises/add";
    }

    // Delete an exercise from the workout plan.
    @GetMapping("/{workoutPlanId}/exercises/delete/{wpExId}")
    public String deleteWorkoutPlanExercise(@PathVariable Long workoutPlanId,
                                            @PathVariable Long wpExId) {
        // Use service method to delete the exercise directly instead of manipulating the
        // lazy-loaded exercises collection on the WorkoutPlan. This avoids triggering
        // LazyInitializationException.
        workoutPlanService.removeExerciseFromWorkoutPlan(workoutPlanId, wpExId);
        return "redirect:/workout-plans/" + workoutPlanId + "/exercises";
    }
}







