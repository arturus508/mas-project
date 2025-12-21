package com.example.healthfitness.service;

import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.model.MealType;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.MealPlanRepository;
import com.example.healthfitness.repository.MealRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.form.MealForm;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for {@link Meal} entities.  Encapsulates all CRUD
 * operations and hides persistence details from controllers.  This
 * implementation also exposes convenience methods for creating meals
 * from a {@link MealForm} DTO and ensures bidirectional associations
 * are maintained when linking meals to meal plans.  Missing entities
 * are signalled by {@link ResourceNotFoundException} so that a
 * global exception handler can render appropriate error pages.
 */
@Service
public class MealService {

    private final MealRepository mealRepository;
    private final MealPlanRepository mealPlanRepository;
    private final UserRepository userRepository;

    public MealService(MealRepository mealRepository,
                       MealPlanRepository mealPlanRepository,
                       UserRepository userRepository) {
        this.mealRepository     = mealRepository;
        this.mealPlanRepository = mealPlanRepository;
        this.userRepository     = userRepository;
    }

    /**
     * Return all meals associated with the given meal plan id.
     */
    public List<Meal> getMealsByMealPlan(Long mealPlanId) {
        return mealRepository.findByMealPlan_MealPlanId(mealPlanId);
    }

    /**
     * Look up a meal by id.  Throws {@link ResourceNotFoundException}
     * when the meal cannot be found.
     */
    public Meal getMealById(Long mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meal not found with id: " + mealId));
    }

    /**
     * Persist a new meal entity and associate it with a user and meal plan.
     * This method is retained for backwards compatibility but is not used
     * in the MVC layer once {@link MealForm} is adopted.
     */
    public Meal saveMeal(Meal meal, Long userId, Long mealPlanId) {
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("MealPlan not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        meal.setMealPlan(mealPlan);
        meal.setUser(user);
        // maintain bidirectional relationship
        mealPlan.getMeals().add(meal);
        return mealRepository.save(meal);
    }

    /**
     * Create and persist a new {@link Meal} from a form object.  This
     * convenience method hides entity construction and association logic
     * from the controller layer.  The provided user id must refer to an
     * existing user.  If the form specifies a meal plan id, the meal
     * will be associated with that plan; otherwise it will remain
     * unattached.  Defaults for date and type are applied when the
     * corresponding form fields are null.
     *
     * @param form   the form object containing user input
     * @param userId id of the user creating the meal
     * @return the persisted meal entity
     */
    public Meal saveMeal(MealForm form, Long userId) {
        Meal meal = new Meal();
        // date defaults to today if not provided
        LocalDate date = form.getDate() != null ? form.getDate() : LocalDate.now();
        MealType type = form.getType() != null ? form.getType() : MealType.BREAKFAST;
        meal.setDate(date);
        meal.setMealType(type);
        meal.setKcalManual(form.getKcal());
        meal.setProteinManual(form.getProtein());
        meal.setFatManual(form.getFat());
        meal.setCarbsManual(form.getCarbs());
        // assign user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        meal.setUser(user);
        // associate meal plan if provided
        Long mealPlanId = form.getMealPlanId();
        if (mealPlanId != null) {
            MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "MealPlan not found with id: " + mealPlanId));
            meal.setMealPlan(mealPlan);
            // maintain bidirectional collection to support orphanRemoval
            mealPlan.getMeals().add(meal);
        }
        return mealRepository.save(meal);
    }

    /**
     * Persist simple updates (e.g. updating macro values or date).  The
     * provided meal must already be managed by JPA or have a valid id.
     */
    public Meal updateMeal(Meal meal) {
        return mealRepository.save(meal);
    }

    /**
     * Delete a meal.  Prior to removal the meal is detached from its
     * parent meal plan collection so that {@link jakarta.persistence.CascadeType#ORPHAN_REMOVAL}
     * can trigger correctly.  Throws if the meal does not exist.
     */
    public void deleteMeal(Long mealId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meal not found with id: " + mealId));
        MealPlan mealPlan = meal.getMealPlan();
        if (mealPlan != null) {
            mealPlan.getMeals().remove(meal);
            // save the plan to update the association
            mealPlanRepository.save(mealPlan);
        }
        mealRepository.delete(meal);
    }
}