package com.example.healthfitness.model;

import jakarta.persistence.*;
import com.example.healthfitness.model.Unit;

@Entity
@Table(name = "food_item")
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true , length = 120)
    private String name;

    /**
     * Unit in which this food item is measured. Using an enum ensures
     * consistent values (G or PIECE) instead of arbitrary strings.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 16)
    private Unit unit;     // G or PIECE

    @Column(nullable = false)
    private Integer kcal100;

    @Column(nullable = false)
    private Integer protein100;

    @Column(nullable = false)
    private Integer fat100;

    @Column(nullable = false)
    private Integer carbs100;

    public FoodItem() {}

    public Long getId() { return id; }                         public void setId(Long id) { this.id = id; }
    public String getName() { return name; }                   public void setName(String name) { this.name = name; }
    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }
    public Integer getKcal100() { return kcal100; }            public void setKcal100(Integer kcal100) { this.kcal100 = kcal100; }
    public Integer getProtein100() { return protein100; }      public void setProtein100(Integer protein100) { this.protein100 = protein100; }
    public Integer getFat100() { return fat100; }              public void setFat100(Integer fat100) { this.fat100 = fat100; }
    public Integer getCarbs100() { return carbs100; }          public void setCarbs100(Integer carbs100) { this.carbs100 = carbs100; }
}
