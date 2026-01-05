package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.User;
import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanExercise;
import com.example.healthfitness.repository.DailyWorkoutRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.repository.WorkoutPlanExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for {@link WorkoutPlan} and {@link WorkoutPlanExercise}
 * persistence and business logic.  Controllers should delegate
 * workout plan operations to this service rather than interacting
 * directly with repositories.
 */
@Service
public class WorkoutPlanService {

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;
    @Autowired
    private WorkoutPlanExerciseRepository workoutPlanExerciseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DailyWorkoutRepository dailyWorkoutRepository;

    /**
     * Retrieve all workout plans.  Typically used only for
     * administrative views.
     */
    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }

    /**
     * Retrieve workout plans belonging to a specific user.
     *
     * @param userId id of the user
     * @return list of workout plans
     */
    public List<WorkoutPlan> getWorkoutPlansByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return workoutPlanRepository.findByUser_UserId(userId);
    }

    /**
     * Find a workout plan by id or throw a runtime exception if not
     * found.
     */
    public WorkoutPlan getWorkoutPlanById(Long workoutPlanId) {
        return workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with ID: " + workoutPlanId));
    }

    public WorkoutPlan getWorkoutPlanByIdForUser(Long userId, Long workoutPlanId) {
        WorkoutPlan plan = getWorkoutPlanById(workoutPlanId);
        if (!plan.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan does not belong to current user");
        }
        return plan;
    }

    /**
     * Persist a new workout plan entity.  Controllers should prefer
     * using {@link #saveWorkoutPlan(com.example.healthfitness.web.form.WorkoutPlanForm, Long)}
     * to save a plan from a form.
     */
    public WorkoutPlan saveWorkoutPlan(WorkoutPlan workoutPlan) {
        return workoutPlanRepository.save(workoutPlan);
    }

    /**
     * Create and persist a WorkoutPlan from the given form DTO.  The
     * plan is associated with the user identified by the supplied
     * user id.  A runtime exception is thrown if the user id does not
     * exist.
     *
     * @param form   the form containing plan details
     * @param userId id of the user creating the plan
     * @return the persisted WorkoutPlan
     */
    public WorkoutPlan saveWorkoutPlan(com.example.healthfitness.web.form.WorkoutPlanForm form, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        WorkoutPlan plan = new WorkoutPlan();
        plan.setPlanName(form.getPlanName());
        plan.setDescription(form.getDescription());
        plan.setStartDate(form.getStartDate());
        plan.setEndDate(form.getEndDate());
        plan.setDaysPerWeek(form.getDaysPerWeek());
        plan.setUser(user);
        return workoutPlanRepository.save(plan);
    }

    /**
     * Delete a workout plan by id.  If the id does not exist the
     * repository will silently ignore the call.
     */
    @Transactional
    public void deleteWorkoutPlan(Long workoutPlanId) {
        WorkoutPlan plan = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with ID: " + workoutPlanId));
        dailyWorkoutRepository.clearPlanDayForWorkoutPlan(plan.getWorkoutPlanId());
        workoutPlanRepository.delete(plan);
    }

    /**
     * Update the fields of an existing workout plan using values
     * supplied by the form DTO.  The plan is looked up by id and
     * updated in place; if not found a runtime exception is thrown.
     *
     * @param workoutPlanId id of the plan to update
     * @param form          form containing updated values
     * @return the updated WorkoutPlan entity
     */
    public WorkoutPlan updateWorkoutPlan(Long workoutPlanId, com.example.healthfitness.web.form.WorkoutPlanForm form) {
        return workoutPlanRepository.findById(workoutPlanId).map(plan -> {
            plan.setPlanName(form.getPlanName());
            plan.setDescription(form.getDescription());
            plan.setStartDate(form.getStartDate());
            plan.setEndDate(form.getEndDate());
            plan.setDaysPerWeek(form.getDaysPerWeek());
            return workoutPlanRepository.save(plan);
        }).orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with ID: " + workoutPlanId));
    }

    public WorkoutPlan updateWorkoutPlanForUser(Long userId, Long workoutPlanId, com.example.healthfitness.web.form.WorkoutPlanForm form) {
        WorkoutPlan plan = getWorkoutPlanByIdForUser(userId, workoutPlanId);
        plan.setPlanName(form.getPlanName());
        plan.setDescription(form.getDescription());
        plan.setStartDate(form.getStartDate());
        plan.setEndDate(form.getEndDate());
        plan.setDaysPerWeek(form.getDaysPerWeek());
        return workoutPlanRepository.save(plan);
    }

    /**
     * Directly update a plan using another plan entity.  This method
     * remains for backwards compatibility but is no longer used by
     * controllers in the clean backend.
     */
    public WorkoutPlan updateWorkoutPlan(Long workoutPlanId, WorkoutPlan updatedWorkoutPlan) {
        return workoutPlanRepository.findById(workoutPlanId).map(existingPlan -> {
            existingPlan.setPlanName(updatedWorkoutPlan.getPlanName());
            existingPlan.setDescription(updatedWorkoutPlan.getDescription());
            existingPlan.setStatus(updatedWorkoutPlan.getStatus());
            existingPlan.setStartDate(updatedWorkoutPlan.getStartDate());
            existingPlan.setEndDate(updatedWorkoutPlan.getEndDate());
            existingPlan.setDaysPerWeek(updatedWorkoutPlan.getDaysPerWeek());
            return workoutPlanRepository.save(existingPlan);
        }).orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with ID: " + workoutPlanId));
    }

    /**
     * Add an exercise to a workout plan.  The supplied
     * {@code WorkoutPlanExercise} should have its exercise property set
     * before calling this method.  The relationship between the plan
     * and the new exercise is managed here.
     */
    public WorkoutPlanExercise addExerciseToWorkoutPlan(Long workoutPlanId, WorkoutPlanExercise workoutPlanExercise) {
        WorkoutPlan workoutPlan = getWorkoutPlanById(workoutPlanId);
        workoutPlanExercise.setWorkoutPlan(workoutPlan);
        return workoutPlanExerciseRepository.save(workoutPlanExercise);
    }

    public WorkoutPlanExercise addExerciseToWorkoutPlan(Long userId, Long workoutPlanId, WorkoutPlanExercise workoutPlanExercise) {
        WorkoutPlan workoutPlan = getWorkoutPlanById(workoutPlanId);
        if (!workoutPlan.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan does not belong to current user");
        }
        workoutPlanExercise.setWorkoutPlan(workoutPlan);
        return workoutPlanExerciseRepository.save(workoutPlanExercise);
    }

    /**
     * Retrieve the list of exercises attached to a workout plan.  The
     * repository method avoids triggering lazy loading on the plan
     * entity, which could lead to {@code LazyInitializationException}.
     */
    public List<WorkoutPlanExercise> getExercisesByWorkoutPlan(Long workoutPlanId) {
        return workoutPlanExerciseRepository.findWithExerciseByWorkoutPlan_WorkoutPlanId(workoutPlanId);
    }

    public List<WorkoutPlanExercise> getExercisesByWorkoutPlan(Long userId, Long workoutPlanId) {
        WorkoutPlan workoutPlan = getWorkoutPlanById(workoutPlanId);
        if (!workoutPlan.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan does not belong to current user");
        }
        return workoutPlanExerciseRepository.findWithExerciseByWorkoutPlan_WorkoutPlanId(workoutPlanId);
    }

    /**
     * Remove an exercise from a workout plan by id.  The unused
     * workoutPlanId parameter is retained for symmetry and future use.
     */
    public void removeExerciseFromWorkoutPlan(Long workoutPlanId, Long wpExId) {
        WorkoutPlanExercise exercise = workoutPlanExerciseRepository.findById(wpExId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlanExercise not found with id: " + wpExId));
        workoutPlanExerciseRepository.delete(exercise);
    }

    public void removeExerciseFromWorkoutPlan(Long userId, Long workoutPlanId, Long wpExId) {
        WorkoutPlan plan = getWorkoutPlanByIdForUser(userId, workoutPlanId);
        WorkoutPlanExercise exercise = workoutPlanExerciseRepository.findById(wpExId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlanExercise not found with id: " + wpExId));
        if (!exercise.getWorkoutPlan().getWorkoutPlanId().equals(plan.getWorkoutPlanId())) {
            throw new ResourceNotFoundException("WorkoutPlanExercise not found with id: " + wpExId);
        }
        workoutPlanExerciseRepository.delete(exercise);
    }
}
