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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;

    @Column(length = 255)
    private String name;

    @Column
    private Integer kcal;

    @Column
    private Integer protein;

    @Column
    private Integer fat;

    @Column
    private Integer carbs;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 16)
    private MealItemSourceType sourceType;

    @Column(nullable = false)
    private Integer quantity;     // if unit=g -> grams ; if unit=piece -> pieces

    public MealItem() {}

    public Long getId() { return id; }                       public void setId(Long id) { this.id = id; }
    public Meal getMeal() { return meal; }                   public void setMeal(Meal meal) { this.meal = meal; }
    public FoodItem getFoodItem() { return foodItem; }       public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }
    public String getName() { return name; }                 public void setName(String name) { this.name = name; }
    public Integer getKcal() { return kcal; }                public void setKcal(Integer kcal) { this.kcal = kcal; }
    public Integer getProtein() { return protein; }          public void setProtein(Integer protein) { this.protein = protein; }
    public Integer getFat() { return fat; }                  public void setFat(Integer fat) { this.fat = fat; }
    public Integer getCarbs() { return carbs; }              public void setCarbs(Integer carbs) { this.carbs = carbs; }
    public MealItemSourceType getSourceType() { return sourceType; } public void setSourceType(MealItemSourceType sourceType) { this.sourceType = sourceType; }
    public Integer getQuantity() { return quantity; }        public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
