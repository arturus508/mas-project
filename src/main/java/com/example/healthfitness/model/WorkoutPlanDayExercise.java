package com.example.healthfitness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "workout_plan_day_exercise")
public class WorkoutPlanDayExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planDayExerciseId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_day_id")
    @JsonBackReference(value = "planDayExercises")
    private WorkoutPlanDay planDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "sets_planned", nullable = false)
    private Integer setsPlanned;

    @Column(name = "target_reps")
    private Integer targetReps;

    @Column(name = "rest_time")
    private Integer restTime;

    public Long getPlanDayExerciseId() { return planDayExerciseId; }
    public void setPlanDayExerciseId(Long planDayExerciseId) { this.planDayExerciseId = planDayExerciseId; }

    public WorkoutPlanDay getPlanDay() { return planDay; }
    public void setPlanDay(WorkoutPlanDay planDay) { this.planDay = planDay; }

    public Exercise getExercise() { return exercise; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }

    public Integer getSetsPlanned() { return setsPlanned; }
    public void setSetsPlanned(Integer setsPlanned) { this.setsPlanned = setsPlanned; }

    public Integer getTargetReps() { return targetReps; }
    public void setTargetReps(Integer targetReps) { this.targetReps = targetReps; }

    public Integer getRestTime() { return restTime; }
    public void setRestTime(Integer restTime) { this.restTime = restTime; }
}
