package com.example.healthfitness.model;

import jakarta.persistence.*;

@Entity
@Table(name = "habit")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HabitCadence cadence;

    @Column(nullable = false)
    private Integer targetPerDay = 1;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Habit(){}

    public Long getId(){ return id; }                      public void setId(Long id){ this.id = id; }
    public String getName(){ return name; }                public void setName(String name){ this.name = name; }
    public HabitCadence getCadence(){ return cadence; }    public void setCadence(HabitCadence cadence){ this.cadence = cadence; }
    public Integer getTargetPerDay(){ return targetPerDay; }  public void setTargetPerDay(Integer targetPerDay){ this.targetPerDay = targetPerDay; }
    public User getUser(){ return user; }                  public void setUser(User user){ this.user = user; }
}

