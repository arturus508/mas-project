package com.example.healthfitness.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutPlanId;

    private String planName;
    private String description;
    private String status; // e.g., "active", "completed"
    private String startDate;
    private String endDate;
    private Integer daysPerWeek;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<WorkoutPlanExercise> workoutPlanExercises = new ArrayList<>();

    // Getters and setters
    public Long getWorkoutPlanId() { return workoutPlanId; }
    public void setWorkoutPlanId(Long workoutPlanId) { this.workoutPlanId = workoutPlanId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public Integer getDaysPerWeek() { return daysPerWeek; }
    public void setDaysPerWeek(Integer daysPerWeek) { this.daysPerWeek = daysPerWeek; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<WorkoutPlanExercise> getWorkoutPlanExercises() { return workoutPlanExercises; }
    public void setWorkoutPlanExercises(List<WorkoutPlanExercise> workoutPlanExercises) { this.workoutPlanExercises = workoutPlanExercises; }

    public void addWorkoutPlanExercise(WorkoutPlanExercise wpe) {
        workoutPlanExercises.add(wpe);
        wpe.setWorkoutPlan(this);
    }

    public void removeWorkoutPlanExercise(WorkoutPlanExercise wpe) {
        workoutPlanExercises.remove(wpe);
        wpe.setWorkoutPlan(null);
    }
}





