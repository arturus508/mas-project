package com.example.healthfitness.repository;

import com.example.healthfitness.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
