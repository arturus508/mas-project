package com.example.healthfitness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_plan_day")
public class WorkoutPlanDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planDayId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id")
    @JsonBackReference(value = "planDays")
    private WorkoutPlan workoutPlan;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(name = "day_order", nullable = false)
    private Integer dayOrder;

    @OneToMany(mappedBy = "planDay", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "planDayExercises")
    private List<WorkoutPlanDayExercise> exercises = new ArrayList<>();

    public Long getPlanDayId() { return planDayId; }
    public void setPlanDayId(Long planDayId) { this.planDayId = planDayId; }

    public WorkoutPlan getWorkoutPlan() { return workoutPlan; }
    public void setWorkoutPlan(WorkoutPlan workoutPlan) { this.workoutPlan = workoutPlan; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getDayOrder() { return dayOrder; }
    public void setDayOrder(Integer dayOrder) { this.dayOrder = dayOrder; }

    public List<WorkoutPlanDayExercise> getExercises() { return exercises; }
    public void setExercises(List<WorkoutPlanDayExercise> exercises) { this.exercises = exercises; }
}
