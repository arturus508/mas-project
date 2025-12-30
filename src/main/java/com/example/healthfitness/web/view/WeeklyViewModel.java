package com.example.healthfitness.web.view;

import java.time.LocalDate;
import java.util.Map;

public class WeeklyViewModel {

    private LocalDate weekStart;
    private LocalDate weekEnd;

    private Integer avgSleepMinutes;
    private Integer avgSleepQuality;

    private Integer avgKcal;
    private Integer avgProtein;
    private Integer avgFat;
    private Integer avgCarbs;

    private int workoutsCount;
    private Map<String, Integer> muscleGroupCounts;

    private long tasksDone;
    private long tasksTotal;

    private int habitsDone;
    private int habitsPossible;

    private Integer avgMood;
    private Integer avgEnergy;
    private int reviewsCount;

    public LocalDate getWeekStart() { return weekStart; }
    public void setWeekStart(LocalDate weekStart) { this.weekStart = weekStart; }

    public LocalDate getWeekEnd() { return weekEnd; }
    public void setWeekEnd(LocalDate weekEnd) { this.weekEnd = weekEnd; }

    public Integer getAvgSleepMinutes() { return avgSleepMinutes; }
    public void setAvgSleepMinutes(Integer avgSleepMinutes) { this.avgSleepMinutes = avgSleepMinutes; }

    public Integer getAvgSleepQuality() { return avgSleepQuality; }
    public void setAvgSleepQuality(Integer avgSleepQuality) { this.avgSleepQuality = avgSleepQuality; }

    public Integer getAvgKcal() { return avgKcal; }
    public void setAvgKcal(Integer avgKcal) { this.avgKcal = avgKcal; }

    public Integer getAvgProtein() { return avgProtein; }
    public void setAvgProtein(Integer avgProtein) { this.avgProtein = avgProtein; }

    public Integer getAvgFat() { return avgFat; }
    public void setAvgFat(Integer avgFat) { this.avgFat = avgFat; }

    public Integer getAvgCarbs() { return avgCarbs; }
    public void setAvgCarbs(Integer avgCarbs) { this.avgCarbs = avgCarbs; }

    public int getWorkoutsCount() { return workoutsCount; }
    public void setWorkoutsCount(int workoutsCount) { this.workoutsCount = workoutsCount; }

    public Map<String, Integer> getMuscleGroupCounts() { return muscleGroupCounts; }
    public void setMuscleGroupCounts(Map<String, Integer> muscleGroupCounts) { this.muscleGroupCounts = muscleGroupCounts; }

    public long getTasksDone() { return tasksDone; }
    public void setTasksDone(long tasksDone) { this.tasksDone = tasksDone; }

    public long getTasksTotal() { return tasksTotal; }
    public void setTasksTotal(long tasksTotal) { this.tasksTotal = tasksTotal; }

    public int getHabitsDone() { return habitsDone; }
    public void setHabitsDone(int habitsDone) { this.habitsDone = habitsDone; }

    public int getHabitsPossible() { return habitsPossible; }
    public void setHabitsPossible(int habitsPossible) { this.habitsPossible = habitsPossible; }

    public Integer getAvgMood() { return avgMood; }
    public void setAvgMood(Integer avgMood) { this.avgMood = avgMood; }

    public Integer getAvgEnergy() { return avgEnergy; }
    public void setAvgEnergy(Integer avgEnergy) { this.avgEnergy = avgEnergy; }

    public int getReviewsCount() { return reviewsCount; }
    public void setReviewsCount(int reviewsCount) { this.reviewsCount = reviewsCount; }
}
