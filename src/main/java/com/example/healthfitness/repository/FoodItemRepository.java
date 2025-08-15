package com.example.healthfitness.repository;

import com.example.healthfitness.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findTop20ByNameContainingIgnoreCaseOrderByNameAsc(String q);
}
