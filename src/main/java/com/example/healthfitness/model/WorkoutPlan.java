package com.example.healthfitness.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a workout plan.  The plan includes metadata such as
 * name, description, status, start and end dates, and the number of days
 * per week the plan is intended to be followed.  Dates are stored as
 * {@link LocalDate} values to allow proper temporal operations and to
 * ensure type safety throughout the application.  Each plan belongs to
 * a user and may contain many workout plan exercises.  The
 * {@link #workoutPlanExercises} collection is eagerly fetched to avoid
 * LazyInitializationExceptions when rendering templates outside of an
 * open session.
 */
@Entity
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutPlanId;

    private String planName;
    private String description;
    private String status; // e.g., "active", "completed"

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private Integer daysPerWeek;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

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