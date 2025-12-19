package com.example.healthfitness.model;

import jakarta.persistence.*;

/**
 * Entity representing an exercise in the system.  Each exercise has a
 * unique name, a muscle group it primarily targets, an intensity
 * level, and default values for repetitions, rest time and sets.
 * The {@code unique} constraint on the {@link #exerciseName} field
 * ensures that duplicate exercises cannot be inserted into the
 * database.
 */
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;

    @Column(unique = true)
    private String exerciseName;
    private String muscleGroup;
    private String intensityLevel;

    // These fields are in your database schema.  We add default values
    // so that when a new Exercise is created, these columns are set
    // automatically.
    @Column(nullable = false, columnDefinition = "int default 0")
    private int reps;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int restTime;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int sets;

    // Getters and setters

    public Long getExerciseId() {
        return exerciseId;
    }
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getIntensityLevel() {
        return intensityLevel;
    }
    public void setIntensityLevel(String intensityLevel) {
        this.intensityLevel = intensityLevel;
    }

    public int getReps() {
        return reps;
    }
    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRestTime() {
        return restTime;
    }
    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public int getSets() {
        return sets;
    }
    public void setSets(int sets) {
        this.sets = sets;
    }
}