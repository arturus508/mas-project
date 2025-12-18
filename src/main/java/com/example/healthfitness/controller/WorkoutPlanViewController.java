package com.example.healthfitness.controller;

import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.model.User;
import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.service.ExerciseService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.WorkoutPlanService;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.web.form.WorkoutPlanForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/workout-plans")
public class WorkoutPlanViewController {

    private final WorkoutPlanService workoutPlanService;
    private final ExerciseService exerciseService;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public WorkoutPlanViewController(WorkoutPlanService workoutPlanService, ExerciseService exerciseService, UserService userService, CurrentUserService currentUserService) {
        this.workoutPlanService = workoutPlanService;
        this.exerciseService    = exerciseService;
        this.userService        = userService;
        this.currentUserService = currentUserService;
    }

    // --------------------- Workout Plan CRUD ---------------------

    // List workout plans for the logged-in user.
    @GetMapping
    public String showWorkoutPlans(Model model) {
        Long userId = currentUserService.id();
        model.addAttribute("workoutPlans", workoutPlanService.getWorkoutPlansByUser(userId));
        return "workout-plans";
    }

    // Display the Add Workout Plan page.
    @GetMapping("/add")
    public String addWorkoutPlanPage(Model model) {
        model.addAttribute("workoutPlanForm", new WorkoutPlanForm());
        return "add-workout-plan";
    }

    // Process the Add Workout Plan form submission.
    @PostMapping("/add")
    public String addWorkoutPlan(@Valid @ModelAttribute("workoutPlanForm") WorkoutPlanForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "add-workout-plan";
        }
        Long userId = currentUserService.id();
        WorkoutPlan plan = new WorkoutPlan();
        plan.setPlanName(form.getPlanName());
        plan.setDescription(form.getDescription());
        plan.setStartDate(form.getStartDate());
        plan.setEndDate(form.getEndDate());
        plan.setDaysPerWeek(form.getDaysPerWeek());
        plan.setUser(userService.getUserById(userId));
        workoutPlanService.saveWorkoutPlan(plan);
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







