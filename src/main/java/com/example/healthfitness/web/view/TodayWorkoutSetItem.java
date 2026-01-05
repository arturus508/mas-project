package com.example.healthfitness.web.view;

public class TodayWorkoutSetItem {
    private final Long setId;
    private final Long exerciseId;
    private final String exerciseName;
    private final Integer setNumber;
    private final Integer repsDone;

    public TodayWorkoutSetItem(Long setId,
                               Long exerciseId,
                               String exerciseName,
                               Integer setNumber,
                               Integer repsDone) {
        this.setId = setId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.setNumber = setNumber;
        this.repsDone = repsDone;
    }

    public Long getSetId() { return setId; }
    public Long getExerciseId() { return exerciseId; }
    public String getExerciseName() { return exerciseName; }
    public Integer getSetNumber() { return setNumber; }
    public Integer getRepsDone() { return repsDone; }
}
