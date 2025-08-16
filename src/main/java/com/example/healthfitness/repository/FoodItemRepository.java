package com.example.healthfitness.repository;

import com.example.healthfitness.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    Optional<FoodItem> findByNameIgnoreCase(String name);
    List<FoodItem> findTop50ByOrderByNameAsc();
    List<FoodItem> findTop20ByNameContainingIgnoreCaseOrderByNameAsc(String q);
}
