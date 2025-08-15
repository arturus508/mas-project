package com.example.healthfitness.model;

import jakarta.persistence.*;

@Entity
@Table(name = "food_item")
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true , length = 120)
    private String name;

    @Column(nullable = false , length = 16)
    private String unit;     // "g"  or  "piece"

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
    public String getUnit() { return unit; }                   public void setUnit(String unit) { this.unit = unit; }
    public Integer getKcal100() { return kcal100; }            public void setKcal100(Integer kcal100) { this.kcal100 = kcal100; }
    public Integer getProtein100() { return protein100; }      public void setProtein100(Integer protein100) { this.protein100 = protein100; }
    public Integer getFat100() { return fat100; }              public void setFat100(Integer fat100) { this.fat100 = fat100; }
    public Integer getCarbs100() { return carbs100; }          public void setCarbs100(Integer carbs100) { this.carbs100 = carbs100; }
}
