package com.example.healthfitness.web.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TodayViewModel {

    private LocalDate date;
    private Map<String, Integer> mealTotals;
    private String workoutTitle;
    private int workoutSetsTotal;
    private int workoutSetsDone;
    private Long dailyWorkoutId;
    private List<TodayWorkoutSetItem> workoutSets;
    private List<TodayExerciseOption> workoutExercises;
    private List<TodayPlanDayOption> workoutPlanDays;
    private List<TodayFoodOption> mealFoodOptions;
    private Integer sleepHours;
    private Integer sleepMinutes;
    private Integer sleepQuality;
    private Double latestWeight;
    private Double todayWeight;
    private Double todayBodyFatPercent;
    private List<String> weightDates;
    private List<Double> weightValues;
    private int tasksTotal;
    private int tasksDone;
    private int habitsTotal;
    private int habitsDone;
    private List<TodayTaskItem> topTasks;
    private List<TodayHabitItem> habits;
    private boolean reviewPresent;
    private Integer reviewMood;
    private Integer reviewEnergy;
    private String reviewNote;
    private boolean reviewResetDone;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Map<String, Integer> getMealTotals() { return mealTotals; }
    public void setMealTotals(Map<String, Integer> mealTotals) { this.mealTotals = mealTotals; }

    public String getWorkoutTitle() { return workoutTitle; }
    public void setWorkoutTitle(String workoutTitle) { this.workoutTitle = workoutTitle; }

    public int getWorkoutSetsTotal() { return workoutSetsTotal; }
    public void setWorkoutSetsTotal(int workoutSetsTotal) { this.workoutSetsTotal = workoutSetsTotal; }

    public int getWorkoutSetsDone() { return workoutSetsDone; }
    public void setWorkoutSetsDone(int workoutSetsDone) { this.workoutSetsDone = workoutSetsDone; }

    public Long getDailyWorkoutId() { return dailyWorkoutId; }
    public void setDailyWorkoutId(Long dailyWorkoutId) { this.dailyWorkoutId = dailyWorkoutId; }

    public List<TodayWorkoutSetItem> getWorkoutSets() { return workoutSets; }
    public void setWorkoutSets(List<TodayWorkoutSetItem> workoutSets) { this.workoutSets = workoutSets; }

    public List<TodayExerciseOption> getWorkoutExercises() { return workoutExercises; }
    public void setWorkoutExercises(List<TodayExerciseOption> workoutExercises) { this.workoutExercises = workoutExercises; }

    public List<TodayPlanDayOption> getWorkoutPlanDays() { return workoutPlanDays; }
    public void setWorkoutPlanDays(List<TodayPlanDayOption> workoutPlanDays) { this.workoutPlanDays = workoutPlanDays; }

    public List<TodayFoodOption> getMealFoodOptions() { return mealFoodOptions; }
    public void setMealFoodOptions(List<TodayFoodOption> mealFoodOptions) { this.mealFoodOptions = mealFoodOptions; }

    public Integer getSleepHours() { return sleepHours; }
    public void setSleepHours(Integer sleepHours) { this.sleepHours = sleepHours; }

    public Integer getSleepMinutes() { return sleepMinutes; }
    public void setSleepMinutes(Integer sleepMinutes) { this.sleepMinutes = sleepMinutes; }

    public Integer getSleepQuality() { return sleepQuality; }
    public void setSleepQuality(Integer sleepQuality) { this.sleepQuality = sleepQuality; }

    public Double getLatestWeight() { return latestWeight; }
    public void setLatestWeight(Double latestWeight) { this.latestWeight = latestWeight; }

    public Double getTodayWeight() { return todayWeight; }
    public void setTodayWeight(Double todayWeight) { this.todayWeight = todayWeight; }

    public Double getTodayBodyFatPercent() { return todayBodyFatPercent; }
    public void setTodayBodyFatPercent(Double todayBodyFatPercent) { this.todayBodyFatPercent = todayBodyFatPercent; }

    public List<String> getWeightDates() { return weightDates; }
    public void setWeightDates(List<String> weightDates) { this.weightDates = weightDates; }

    public List<Double> getWeightValues() { return weightValues; }
    public void setWeightValues(List<Double> weightValues) { this.weightValues = weightValues; }

    public int getTasksTotal() { return tasksTotal; }
    public void setTasksTotal(int tasksTotal) { this.tasksTotal = tasksTotal; }

    public int getTasksDone() { return tasksDone; }
    public void setTasksDone(int tasksDone) { this.tasksDone = tasksDone; }

    public int getHabitsTotal() { return habitsTotal; }
    public void setHabitsTotal(int habitsTotal) { this.habitsTotal = habitsTotal; }

    public int getHabitsDone() { return habitsDone; }
    public void setHabitsDone(int habitsDone) { this.habitsDone = habitsDone; }

    public List<TodayTaskItem> getTopTasks() { return topTasks; }
    public void setTopTasks(List<TodayTaskItem> topTasks) { this.topTasks = topTasks; }

    public List<TodayHabitItem> getHabits() { return habits; }
    public void setHabits(List<TodayHabitItem> habits) { this.habits = habits; }

    public boolean isReviewPresent() { return reviewPresent; }
    public void setReviewPresent(boolean reviewPresent) { this.reviewPresent = reviewPresent; }

    public Integer getReviewMood() { return reviewMood; }
    public void setReviewMood(Integer reviewMood) { this.reviewMood = reviewMood; }

    public Integer getReviewEnergy() { return reviewEnergy; }
    public void setReviewEnergy(Integer reviewEnergy) { this.reviewEnergy = reviewEnergy; }

    public String getReviewNote() { return reviewNote; }
    public void setReviewNote(String reviewNote) { this.reviewNote = reviewNote; }

    public boolean isReviewResetDone() { return reviewResetDone; }
    public void setReviewResetDone(boolean reviewResetDone) { this.reviewResetDone = reviewResetDone; }
}
