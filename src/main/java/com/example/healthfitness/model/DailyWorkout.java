package com.example.healthfitness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "daily_workout")
public class DailyWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyWorkoutId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_day_id")
    private WorkoutPlanDay planDay;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 64)
    private String title;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "dailyWorkout", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DailyWorkoutSet> sets = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getDailyWorkoutId() { return dailyWorkoutId; }
    public void setDailyWorkoutId(Long dailyWorkoutId) { this.dailyWorkoutId = dailyWorkoutId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public WorkoutPlanDay getPlanDay() { return planDay; }
    public void setPlanDay(WorkoutPlanDay planDay) { this.planDay = planDay; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<DailyWorkoutSet> getSets() { return sets; }
    public void setSets(List<DailyWorkoutSet> sets) { this.sets = sets; }
}
