package com.example.healthfitness.repository;

import com.example.healthfitness.model.MealPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    List<MealPlan> findByUser_UserId(Long userId);

    @EntityGraph(attributePaths = "meals")
    List<MealPlan> findWithMealsByUser_UserId(Long userId);

    @EntityGraph(attributePaths = "meals")
    Optional<MealPlan> findWithMealsByMealPlanId(Long mealPlanId);

    @EntityGraph(attributePaths = "meals")
    Optional<MealPlan> findWithMealsByMealPlanIdAndUser_UserId(Long mealPlanId, Long userId);

}
