package com.example.healthfitness.service;

import com.example.healthfitness.model.MealPlan;
import com.example.healthfitness.repository.MealPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;

    public MealPlanService(MealPlanRepository mealPlanRepository) {
        this.mealPlanRepository = mealPlanRepository;
    }

    public List<MealPlan> getAllMealPlans() {
        return mealPlanRepository.findAll();
    }

    public MealPlan getMealPlanById(Long id) {
        Optional<MealPlan> mealPlan = mealPlanRepository.findById(id);
        return mealPlan.orElse(null);
    }

    public MealPlan saveMealPlan(MealPlan mealPlan) {
        return mealPlanRepository.save(mealPlan);
    }

    public MealPlan updateMealPlan(Long id, MealPlan updatedMealPlan) {
        Optional<MealPlan> existingMealPlanOpt = mealPlanRepository.findById(id);

        if (existingMealPlanOpt.isPresent()) {
            MealPlan existingMealPlan = existingMealPlanOpt.get();
            existingMealPlan.setPlanName(updatedMealPlan.getPlanName());
            existingMealPlan.setDescription(updatedMealPlan.getDescription());
            existingMealPlan.setDietaryRestriction(updatedMealPlan.getDietaryRestriction());
            existingMealPlan.setStartDate(updatedMealPlan.getStartDate());
            existingMealPlan.setEndDate(updatedMealPlan.getEndDate());
            return mealPlanRepository.save(existingMealPlan);
        }
        return null;
    }

    public void deleteMealPlan(Long id) {
        mealPlanRepository.deleteById(id);
    }
}



