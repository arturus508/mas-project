package com.example.healthfitness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a meal plan.  The plan contains a name, optional
 * description and dietary restriction, as well as a start and end date
 * for when the plan is active.  Dates are modelled using {@link LocalDate}
 * rather than {@link String} so that validation and comparisons can be
 * performed cleanly.  The {@link DateTimeFormat} annotations instruct
 * Spring to bind ISO‑8601 (yyyy‑MM‑dd) formatted strings from web forms
 * into {@link LocalDate} values during data binding.  A meal plan belongs
 * to a single user and may have many meals.  The meals collection is
 * eagerly fetched to ensure it is available when rendering views without
 * requiring an open Hibernate session.
 */
@Entity
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealPlanId;

    private String planName;
    private String description;
    private String dietaryRestriction;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The list of meals associated with this plan.  By using
     * {@link FetchType#EAGER} the collection will be loaded along with
     * the meal plan, avoiding LazyInitializationException when
     * rendering Thymeleaf views outside of a transactional context.
     */
    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Meal> meals = new ArrayList<>();

    // Getters and setters

    public Long getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(Long mealPlanId) {
        this.mealPlanId = mealPlanId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDietaryRestriction() {
        return dietaryRestriction;
    }

    public void setDietaryRestriction(String dietaryRestriction) {
        this.dietaryRestriction = dietaryRestriction;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}