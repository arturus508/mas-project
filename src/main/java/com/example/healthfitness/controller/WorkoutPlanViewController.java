package com.example.healthfitness.controller;

import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.DailyWorkoutService;
import com.example.healthfitness.service.ExerciseService;
import com.example.healthfitness.service.WorkoutPlanDayService;
import com.example.healthfitness.service.WorkoutPlanService;
import com.example.healthfitness.web.form.WorkoutPlanDayExerciseForm;
import com.example.healthfitness.web.form.WorkoutPlanForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * MVC controller for workout plan management.  This version
 * separates concerns by binding user input to a {@link
 * WorkoutPlanForm} DTO, delegating business logic to
 * {@link WorkoutPlanService} and leaving persistence access to the
 * repository layer.  It also uses {@link CurrentUserService} to
 * obtain the id of the current user rather than reading directly
 * from the security context.
 */
@Controller
@RequestMapping("/workout-plans")
public class WorkoutPlanViewController {

    private final WorkoutPlanService workoutPlanService;
    private final ExerciseService exerciseService;
    private final CurrentUserService currentUserService;
    private final WorkoutPlanDayService workoutPlanDayService;
    private final DailyWorkoutService dailyWorkoutService;

    public WorkoutPlanViewController(WorkoutPlanService workoutPlanService,
                                     ExerciseService exerciseService,
                                     CurrentUserService currentUserService,
                                     WorkoutPlanDayService workoutPlanDayService,
                                     DailyWorkoutService dailyWorkoutService) {
        this.workoutPlanService = workoutPlanService;
        this.exerciseService    = exerciseService;
        this.currentUserService = currentUserService;
        this.workoutPlanDayService = workoutPlanDayService;
        this.dailyWorkoutService = dailyWorkoutService;
    }

    // --------------------- Workout Plan CRUD ---------------------

    /**
     * List workout plans for the logged in user.  The id is
     * provided by {@link CurrentUserService} and the plans are
     * retrieved via the service layer.
     */
    @GetMapping
    public String showWorkoutPlans(Model model) {
        Long userId = currentUserService.id();
        model.addAttribute("workoutPlans", workoutPlanService.getWorkoutPlansByUser(userId));
        return "workout-plans";
    }

    /**
     * Display the Add Workout Plan page.  An empty {@link
     * WorkoutPlanForm} is added to the model for binding.
     */
    @GetMapping("/add")
    public String addWorkoutPlanPage(Model model) {
        model.addAttribute("workoutPlanForm", new WorkoutPlanForm());
        return "add-workout-plan";
    }

    /**
     * Process the Add Workout Plan form submission.  On validation
     * errors the form is redisplayed.  On success the plan is saved
     * through the service layer and the user is redirected back to
     * the list.
     */
    @PostMapping("/add")
    public String addWorkoutPlan(@Valid @ModelAttribute("workoutPlanForm") WorkoutPlanForm form,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-workout-plan";
        }
        Long userId = currentUserService.id();
        workoutPlanService.saveWorkoutPlan(form, userId);
        return "redirect:/workout-plans";
    }

    /**
     * Display the Edit Workout Plan page.  The existing plan is
     * loaded via the service and mapped into a form object for
     * binding.  The id is also added separately for the form action.
     */
    @GetMapping("/edit/{id}")
    public String editWorkoutPlanPage(@PathVariable Long id, Model model) {
        Long userId = currentUserService.id();
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanByIdForUser(userId, id);
        WorkoutPlanForm form = new WorkoutPlanForm();
        form.setPlanName(workoutPlan.getPlanName());
        form.setDescription(workoutPlan.getDescription());
        form.setStartDate(workoutPlan.getStartDate());
        form.setEndDate(workoutPlan.getEndDate());
        form.setDaysPerWeek(workoutPlan.getDaysPerWeek());
        model.addAttribute("workoutPlanForm", form);
        model.addAttribute("workoutPlanId", id);
        return "edit-workout-plan";
    }

    /**
     * Process the Edit Workout Plan form submission.  Validation
     * errors cause the page to reload; on success the plan is
     * updated via the service and the user is redirected to the
     * list.
     */
    @PostMapping("/edit/{id}")
    public String editWorkoutPlan(@PathVariable Long id,
                                  @Valid @ModelAttribute("workoutPlanForm") WorkoutPlanForm form,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-workout-plan";
        }
        Long userId = currentUserService.id();
        workoutPlanService.updateWorkoutPlanForUser(userId, id, form);
        return "redirect:/workout-plans";
    }

    /**
     * Delete a workout plan.  Uses POST to avoid accidental deletes
     * via bookmark or crawler.  The id is passed as a path
     * variable.
     */
    @PostMapping("/delete/{id}")
    public String deleteWorkoutPlan(@PathVariable Long id) {
        Long userId = currentUserService.id();
        workoutPlanService.getWorkoutPlanByIdForUser(userId, id);
        workoutPlanService.deleteWorkoutPlan(id);
        return "redirect:/workout-plans";
    }

    // --------------------- Workout Plan Exercise Management ---------------------

    /**
     * Display the details of a specific workout plan including its
     * exercises.  Exercises are fetched via the service layer to
     * avoid lazy loading issues on the plan entity.
     */
    @GetMapping("/{id}/exercises")
    public String getWorkoutPlanDetails(@PathVariable Long id, Model model) {
        Long userId = currentUserService.id();
        WorkoutPlan plan = workoutPlanService.getWorkoutPlanByIdForUser(userId, id);
        model.addAttribute("workoutPlan", plan);
        model.addAttribute("planDays", workoutPlanDayService.ensurePlanDays(userId, id, plan.getDaysPerWeek()));
        model.addAttribute("workoutPlanId", id);
        return "workout-plan-details";
    }

    @GetMapping("/{id}/days/{dayId}/exercises/add")
    public String addWorkoutPlanDayExercisePage(@PathVariable("id") Long workoutPlanId,
                                                @PathVariable("dayId") Long dayId,
                                                Model model) {
        Long userId = currentUserService.id();
        workoutPlanService.getWorkoutPlanByIdForUser(userId, workoutPlanId);
        model.addAttribute("workoutPlanId", workoutPlanId);
        model.addAttribute("planDayId", dayId);
        model.addAttribute("workoutPlanDayExerciseForm", new WorkoutPlanDayExerciseForm());
        model.addAttribute("globalExercises", exerciseService.getAllExercises());
        return "workout-plan-day-exercise-form";
    }

    @PostMapping("/{id}/days/{dayId}/exercises/add")
    public String addWorkoutPlanDayExercise(@PathVariable("id") Long workoutPlanId,
                                            @PathVariable("dayId") Long dayId,
                                            @Valid @ModelAttribute("workoutPlanDayExerciseForm") WorkoutPlanDayExerciseForm form,
                                            BindingResult bindingResult,
                                            Model model) {
        Long userId = currentUserService.id();
        workoutPlanService.getWorkoutPlanByIdForUser(userId, workoutPlanId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("workoutPlanId", workoutPlanId);
            model.addAttribute("planDayId", dayId);
            model.addAttribute("globalExercises", exerciseService.getAllExercises());
            return "workout-plan-day-exercise-form";
        }
        workoutPlanDayService.addExerciseToPlanDay(
                userId,
                dayId,
                form.getExerciseId(),
                form.getSetsPlanned(),
                form.getTargetReps(),
                form.getRestTime()
        );
        return "redirect:/workout-plans/" + workoutPlanId + "/exercises";
    }

    @PostMapping("/{id}/days/{dayId}/exercises/delete/{planDayExerciseId}")
    public String deleteWorkoutPlanDayExercise(@PathVariable("id") Long workoutPlanId,
                                               @PathVariable Long dayId,
                                               @PathVariable Long planDayExerciseId) {
        Long userId = currentUserService.id();
        workoutPlanDayService.deletePlanDayExercise(userId, planDayExerciseId);
        return "redirect:/workout-plans/" + workoutPlanId + "/exercises";
    }

    @PostMapping("/days/{dayId}/add-to-today")
    public String addPlanDayToToday(@PathVariable Long dayId) {
        Long userId = currentUserService.id();
        dailyWorkoutService.getOrCreateFromPlanDay(userId, dayId, LocalDate.now());
        return "redirect:/workouts/today";
    }

    /**
     * Display the form to add a new exercise to a workout plan.  A
     * blank {@link WorkoutPlanExercise} is provided for binding and
     * the global exercise list is loaded for the dropdown.
     */
    @GetMapping("/{id}/exercises/add")
    public String addWorkoutPlanExercisePage(@PathVariable("id") Long workoutPlanId, Model model) {
        Long userId = currentUserService.id();
        workoutPlanService.getWorkoutPlanByIdForUser(userId, workoutPlanId);
        WorkoutPlanExercise wpe = new WorkoutPlanExercise();
        wpe.setExercise(new Exercise());
        model.addAttribute("workoutPlanExercise", wpe);
        model.addAttribute("workoutPlanId", workoutPlanId);
        model.addAttribute("globalExercises", exerciseService.getAllExercises());
        return "workout-plan-exercise-form";
    }

    /**
     * Process the form submission to add a new exercise to the
     * workout plan.  After saving we redirect back to the add
     * exercise view so the user can continue adding exercises.
     */
    @PostMapping("/{id}/exercises/add")
    public String addWorkoutPlanExercise(@PathVariable("id") Long workoutPlanId,
                                         @ModelAttribute WorkoutPlanExercise workoutPlanExercise) {
        Long userId = currentUserService.id();
        Long exerciseId = workoutPlanExercise.getExercise().getExerciseId();
        Exercise exercise = exerciseService.getExerciseById(exerciseId);
        workoutPlanExercise.setExercise(exercise);
        workoutPlanService.addExerciseToWorkoutPlan(userId, workoutPlanId, workoutPlanExercise);
        return "redirect:/workout-plans/" + workoutPlanId + "/exercises";
    }

    /**
     * Delete an exercise from the workout plan.  This delegates to
     * the service which performs a direct delete of the
     * WorkoutPlanExercise entity to avoid lazy loading issues.
     */
    @PostMapping("/{workoutPlanId}/exercises/delete/{wpExId}")
    public String deleteWorkoutPlanExercise(@PathVariable Long workoutPlanId,
                                            @PathVariable Long wpExId) {
        Long userId = currentUserService.id();
        workoutPlanService.removeExerciseFromWorkoutPlan(userId, workoutPlanId, wpExId);
        return "redirect:/workout-plans/" + workoutPlanId + "/exercises";
    }
}
