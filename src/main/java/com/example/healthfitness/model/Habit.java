package com.example.healthfitness.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "habit")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false , length = 160)
    private String name;

    @Column(name = "target_per_day" , nullable = false)
    private Integer targetPerDay = 1;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "cadence" , nullable = false , length = 16)
    private String cadence = "DAILY";

    public Habit(){}

    public Long getId(){ return id; }                      public void setId(Long id){ this.id = id; }
    public User getUser(){ return user; }                  public void setUser(User user){ this.user = user; }
    public String getName(){ return name; }                public void setName(String name){ this.name = name; }
    public Integer getTargetPerDay(){ return targetPerDay; } public void setTargetPerDay(Integer targetPerDay){ this.targetPerDay = targetPerDay; }
    public boolean isActive(){ return active; }            public void setActive(boolean active){ this.active = active; }
    public LocalDateTime getCreatedAt(){ return createdAt; } public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }
    public String getCadence(){ return cadence; }          public void setCadence(String cadence){ this.cadence = cadence; }

    @PrePersist
    void pre(){
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (cadence == null || cadence.isBlank()) cadence = "DAILY";
    }
}



