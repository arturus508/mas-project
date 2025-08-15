package com.example.healthfitness.repository;

import com.example.healthfitness.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

   
    Meal findByUserAndDateAndMealType(User user, LocalDate date, MealType mealType);
    List<Meal> findByUserAndDate(User user, LocalDate date);

    
    List<Meal> findByMealPlan_MealPlanId(Long mealPlanId);
}
