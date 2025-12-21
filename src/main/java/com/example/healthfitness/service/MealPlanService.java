package com.example.healthfitness.service;

import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.MealPlanRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.form.MealPlanForm;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for {@link MealPlan} entities.  Encapsulates all CRUD
 * operations and enforces domain invariants.  Controllers should call
 * this service rather than interacting with the {@link MealPlanRepository}
 * directly.  All methods throw {@link ResourceNotFoundException} when a
 * requested entity does not exist so that the {@code GlobalExceptionHandler}
 * can render a 404 page.
 */
@Service
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;
    private final UserRepository userRepository;

    public MealPlanService(MealPlanRepository mealPlanRepository,
                           UserRepository userRepository) {
        this.mealPlanRepository = mealPlanRepository;
        this.userRepository     = userRepository;
    }

    /**
     * Return all meal plans for the given user id.
     */
    public List<MealPlan> getMealPlansByUser(Long userId) {
        return mealPlanRepository.findByUser_UserId(userId);
    }

    /**
     * Look up a meal plan by id.  Throws {@link ResourceNotFoundException}
     * if no plan exists with the given id.
     */
    public MealPlan getMealPlanById(Long id) {
        return mealPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal plan not found with id: " + id));
    }

    /**
     * Persist a new meal plan.  The plan must already have its user set.
     */
    public MealPlan saveMealPlan(MealPlan mealPlan) {
        return mealPlanRepository.save(mealPlan);
    }

    /**
     * Update an existing meal plan from an entity instance.  Throws
     * {@link ResourceNotFoundException} if the plan does not exist.
     * Only mutable fields are updated; the associated user and meals
     * remain intact unless overwritten in the provided object.
     */
    public MealPlan updateMealPlan(Long id, MealPlan updatedMealPlan) {
        MealPlan existingMealPlan = mealPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal plan not found with id: " + id));
        existingMealPlan.setPlanName(updatedMealPlan.getPlanName());
        existingMealPlan.setDescription(updatedMealPlan.getDescription());
        existingMealPlan.setDietaryRestriction(updatedMealPlan.getDietaryRestriction());
        existingMealPlan.setStartDate(updatedMealPlan.getStartDate());
        existingMealPlan.setEndDate(updatedMealPlan.getEndDate());
        existingMealPlan.setUser(updatedMealPlan.getUser());
        return mealPlanRepository.save(existingMealPlan);
    }

    /**
     * Update an existing meal plan using a {@link MealPlanForm}.  The user
     * performing the update must be provided so that ownership can be
     * reassigned if necessary.  Only the form fields are applied; meals
     * and other associations remain unchanged.
     *
     * @param id   the id of the plan to update
     * @param form the form containing new values
     * @param userId   the id of the user performing the update
     * @return the updated meal plan
     */
    public MealPlan updateMealPlan(Long id, MealPlanForm form, Long userId) {
        MealPlan existing = mealPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal plan not found with id: " + id));
        existing.setPlanName(form.getPlanName());
        existing.setDescription(form.getDescription());
        existing.setDietaryRestriction(form.getDietaryRestriction());
        existing.setStartDate(form.getStartDate());
        existing.setEndDate(form.getEndDate());
        // assign the current user to ensure ownership is correct
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        existing.setUser(user);
        return mealPlanRepository.save(existing);
    }

    /**
     * Delete a meal plan by id.  If the plan is not found, a
     * {@link ResourceNotFoundException} is thrown.  Cascade settings on
     * {@link MealPlan} ensure that associated meals are removed via
     * {@link jakarta.persistence.CascadeType#ALL} and
     * {@link jakarta.persistence.CascadeType#ORPHAN_REMOVAL}.
     */
    public void deleteMealPlan(Long id) {
        MealPlan plan = mealPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal plan not found with id: " + id));
        mealPlanRepository.delete(plan);
    }
}