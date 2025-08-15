package com.example.healthfitness.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "meal_plan_id")
    private MealPlan mealPlan;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private MealType mealType;

    @Column(nullable = false)
    private LocalDate date;

    
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealItem> items = new ArrayList<>();

    //  legacy:  stara  lista składników 
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();

    // quick-meal override
    private Integer kcalManual;
    private Integer proteinManual;
    private Integer fatManual;
    private Integer carbsManual;

    public Meal() {}

    public Long getMealId() { return mealId; }
    public void setMealId(Long mealId) { this.mealId = mealId; }

    public MealPlan getMealPlan() { return mealPlan; }
    public void setMealPlan(MealPlan mealPlan) { this.mealPlan = mealPlan; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<MealItem> getItems() { return items; }
    public void setItems(List<MealItem> items) { this.items = items; }

    // legacy getters/setters for old Ingredient flow
    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public Integer getKcalManual() { return kcalManual; }
    public void setKcalManual(Integer kcalManual) { this.kcalManual = kcalManual; }

    public Integer getProteinManual() { return proteinManual; }
    public void setProteinManual(Integer proteinManual) { this.proteinManual = proteinManual; }

    public Integer getFatManual() { return fatManual; }
    public void setFatManual(Integer fatManual) { this.fatManual = fatManual; }

    public Integer getCarbsManual() { return carbsManual; }
    public void setCarbsManual(Integer carbsManual) { this.carbsManual = carbsManual; }
}
