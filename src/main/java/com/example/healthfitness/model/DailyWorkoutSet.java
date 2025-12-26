package com.example.healthfitness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_workout_set")
public class DailyWorkoutSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyWorkoutSetId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_workout_id")
    @JsonBackReference
    private DailyWorkout dailyWorkout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "reps_done")
    private Integer repsDone;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getDailyWorkoutSetId() { return dailyWorkoutSetId; }
    public void setDailyWorkoutSetId(Long dailyWorkoutSetId) { this.dailyWorkoutSetId = dailyWorkoutSetId; }

    public DailyWorkout getDailyWorkout() { return dailyWorkout; }
    public void setDailyWorkout(DailyWorkout dailyWorkout) { this.dailyWorkout = dailyWorkout; }

    public Exercise getExercise() { return exercise; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }

    public Integer getSetNumber() { return setNumber; }
    public void setSetNumber(Integer setNumber) { this.setNumber = setNumber; }

    public Integer getRepsDone() { return repsDone; }
    public void setRepsDone(Integer repsDone) { this.repsDone = repsDone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
