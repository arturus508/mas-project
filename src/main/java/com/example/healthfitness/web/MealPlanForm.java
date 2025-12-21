package com.example.healthfitness.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Form backing object for creating or editing a meal plan.  Using a
 * separate DTO decouples the web layer from the persistence model
 * and allows us to perform validation on user input.  The entity
 * {@link com.example.healthfitness.model.MealPlan} contains more
 * fields but not all of them are required from the user on the form.
 */
public class MealPlanForm {

    @NotBlank
    private String planName;

    private String description;

    private String dietaryRestriction;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    // getters and setters
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDietaryRestriction() { return dietaryRestriction; }
    public void setDietaryRestriction(String dietaryRestriction) { this.dietaryRestriction = dietaryRestriction; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}