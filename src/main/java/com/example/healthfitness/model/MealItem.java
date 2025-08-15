package com.example.healthfitness.model;

import jakarta.persistence.*;

@Entity
@Table(name = "meal_item")
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;

    @Column(nullable = false)
    private Integer quantity;     // if unit=g -> grams ; if unit=piece -> pieces

    public MealItem() {}

    public Long getId() { return id; }                       public void setId(Long id) { this.id = id; }
    public Meal getMeal() { return meal; }                   public void setMeal(Meal meal) { this.meal = meal; }
    public FoodItem getFoodItem() { return foodItem; }       public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }
    public Integer getQuantity() { return quantity; }        public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
