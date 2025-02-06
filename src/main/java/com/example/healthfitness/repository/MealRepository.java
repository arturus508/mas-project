package com.example.healthfitness.repository;

import com.example.healthfitness.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUser_UserId(Long userId);
    List<Meal> findByMealPlan_MealPlanId(Long mealPlanId);
}
