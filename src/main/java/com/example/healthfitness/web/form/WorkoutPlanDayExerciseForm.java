package com.example.healthfitness.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WorkoutPlanDayExerciseForm {

    @NotNull
    private Long exerciseId;

    @NotNull
    @Min(1)
    private Integer setsPlanned;

    private Integer targetReps;
    private Integer restTime;

    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }

    public Integer getSetsPlanned() { return setsPlanned; }
    public void setSetsPlanned(Integer setsPlanned) { this.setsPlanned = setsPlanned; }

    public Integer getTargetReps() { return targetReps; }
    public void setTargetReps(Integer targetReps) { this.targetReps = targetReps; }

    public Integer getRestTime() { return restTime; }
    public void setRestTime(Integer restTime) { this.restTime = restTime; }
}
