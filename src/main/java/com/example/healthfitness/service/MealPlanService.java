package com.example.healthfitness.service;

import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.repository.MealPlanRepository;
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

    public MealPlanService(MealPlanRepository mealPlanRepository) {
        this.mealPlanRepository = mealPlanRepository;
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
     * Update an existing meal plan.  Throws {@link ResourceNotFoundException}
     * if the plan does not exist.  Only mutable fields are updated; the
     * associated user and meals remain intact unless overwritten in the
     * provided object.
     */
    public MealPlan updateMealPlan(Long id, MealPlan updatedMealPlan) {
        MealPlan existingMealPlan = mealPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal plan not found with id: " + id));
        existingMealPlan.setPlanName(updatedMealPlan.getPlanName());
        existingMealPlan.setDescription(updatedMealPlan.getDescription());
        existingMealPlan.setDietaryRestriction(updatedMealPlan.getDietaryRestriction());
        existingMealPlan.setStartDate(updatedMealPlan.getStartDate());
        existingMealPlan.setEndDate(updatedMealPlan.getEndDate());
        // update user in case it changed (should be current user)
        existingMealPlan.setUser(updatedMealPlan.getUser());
        return mealPlanRepository.save(existingMealPlan);
    }

    /**
     * Delete a meal plan by id.  If the plan is not found, a
     * {@link ResourceNotFoundException} is thrown.  Cascade settings on
     * {@link MealPlan} ensure that associated meals are removed via
     * {@link jakarta.persistence.CascadeType#ALL} and
     * {@link jakarta.persistence.CascadeType#ORPHAN_REMOVAL}.
     */
    public void deleteMealPlan(Long id) {
        // verify existence before delete to provide consistent error handling
        MealPlan plan = mealPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal plan not found with id: " + id));
        mealPlanRepository.delete(plan);
    }
}