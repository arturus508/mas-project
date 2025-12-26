package com.example.healthfitness.repository;

import com.example.healthfitness.model.MealItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealItemRepository extends JpaRepository<MealItem, Long> {
    List<MealItem> findByMeal_MealId(Long mealId);

    @Query("select mi from MealItem mi left join fetch mi.foodItem where mi.meal.mealId = :mealId")
    List<MealItem> findByMealIdWithFoodItem(@Param("mealId") Long mealId);
}
