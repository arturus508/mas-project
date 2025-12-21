package com.example.healthfitness.web.form;

import com.example.healthfitness.model.MealType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Data transfer object used as the form backing bean when creating a meal
 * manually.  Using a dedicated DTO decouples the web layer from the
 * persistence model and allows us to perform validation on user input
 * without exposing internal entity classes.  Fields correspond to the
 * user‑editable properties on {@link com.example.healthfitness.model.Meal}.
 */
public class MealForm {

    /**
     * The date the meal occurs.  A value is required at the form level
     * and will default to the current day in the controller if not
     * specified by the user.
     */
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    /**
     * The type of meal (BREAKFAST, LUNCH, DINNER, SNACK).  Defaults to
     * BREAKFAST when not provided.
     */
    private MealType type;

    /**
     * Optional manual calories entry.  If null, the calories will be
     * computed from protein, fat and carbs when persisting the meal.
     */
    @Min(value = 0, message = "Calories cannot be negative")
    private Integer kcal;

    /**
     * Optional protein value in grams.  Non‑negative.
     */
    @Min(value = 0, message = "Protein cannot be negative")
    private Integer protein;

    /**
     * Optional fat value in grams.  Non‑negative.
     */
    @Min(value = 0, message = "Fat cannot be negative")
    private Integer fat;

    /**
     * Optional carbohydrate value in grams.  Non‑negative.
     */
    @Min(value = 0, message = "Carbs cannot be negative")
    private Integer carbs;

    /**
     * The id of the meal plan to which this meal should be attached.  May
     * be null if the user is adding a stand‑alone meal for the daily
     * dashboard.
     */
    private Long mealPlanId;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
    }

    public Integer getKcal() {
        return kcal;
    }

    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

    public Long getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(Long mealPlanId) {
        this.mealPlanId = mealPlanId;
    }
}