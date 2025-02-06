package com.example.healthfitness.service;

import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.MealRepository;
import com.example.healthfitness.repository.MealPlanRepository;
import com.example.healthfitness.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final MealPlanRepository mealPlanRepository;
    private final UserRepository userRepository;

    public MealService(MealRepository mealRepository, MealPlanRepository mealPlanRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.mealPlanRepository = mealPlanRepository;
        this.userRepository = userRepository;
    }

    public List<Meal> getMealsByMealPlan(Long mealPlanId) {
        return mealRepository.findByMealPlan_MealPlanId(mealPlanId);
    }

    public Meal getMealById(Long mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found with id: " + mealId));
    }

    public Meal saveMeal(Meal meal, Long userId, Long mealPlanId) {
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new RuntimeException("MealPlan not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        meal.setMealPlan(mealPlan);
        meal.setUser(user);
        return mealRepository.save(meal);
    }

    // Use this method for simple updates (such as adding or removing ingredients)
    public Meal updateMeal(Meal meal) {
        return mealRepository.save(meal);
    }

    public void deleteMeal(Long mealId) {
        mealRepository.deleteById(mealId);
    }
}




