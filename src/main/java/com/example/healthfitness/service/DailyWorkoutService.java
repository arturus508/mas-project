package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.DailyWorkoutRepository;
import com.example.healthfitness.repository.DailyWorkoutSetRepository;
import com.example.healthfitness.repository.ExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanDayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyWorkoutService {

    private final DailyWorkoutRepository dailyWorkoutRepository;
    private final DailyWorkoutSetRepository dailyWorkoutSetRepository;
    private final WorkoutPlanDayRepository workoutPlanDayRepository;
    private final ExerciseRepository exerciseRepository;

    public DailyWorkoutService(DailyWorkoutRepository dailyWorkoutRepository,
                               DailyWorkoutSetRepository dailyWorkoutSetRepository,
                               WorkoutPlanDayRepository workoutPlanDayRepository,
                               ExerciseRepository exerciseRepository) {
        this.dailyWorkoutRepository = dailyWorkoutRepository;
        this.dailyWorkoutSetRepository = dailyWorkoutSetRepository;
        this.workoutPlanDayRepository = workoutPlanDayRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public DailyWorkout getOrCreateFromPlanDay(Long userId, Long planDayId, LocalDate date) {
        LocalDate targetDate = date == null ? LocalDate.now() : date;
        DailyWorkout existing = dailyWorkoutRepository.findByUser_UserIdAndDate(userId, targetDate).orElse(null);
        if (existing != null) {
            return existing;
        }

        WorkoutPlanDay planDay = workoutPlanDayRepository.findWithExercisesByPlanDayId(planDayId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan day not found with ID: " + planDayId));
        if (!planDay.getWorkoutPlan().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan does not belong to current user");
        }

        DailyWorkout workout = new DailyWorkout();
        workout.setUser(planDay.getWorkoutPlan().getUser());
        workout.setPlanDay(planDay);
        workout.setDate(targetDate);
        workout.setTitle(planDay.getName());
        DailyWorkout saved = dailyWorkoutRepository.save(workout);

        List<DailyWorkoutSet> sets = new ArrayList<>();
        for (WorkoutPlanDayExercise planExercise : planDay.getExercises()) {
            int planned = planExercise.getSetsPlanned() == null ? 0 : planExercise.getSetsPlanned();
            for (int i = 1; i <= planned; i++) {
                DailyWorkoutSet set = new DailyWorkoutSet();
                set.setDailyWorkout(saved);
                set.setExercise(planExercise.getExercise());
                set.setSetNumber(i);
                sets.add(set);
            }
        }
        dailyWorkoutSetRepository.saveAll(sets);
        return saved;
    }

    public DailyWorkout getForDate(Long userId, LocalDate date) {
        LocalDate targetDate = date == null ? LocalDate.now() : date;
        return dailyWorkoutRepository.findByUser_UserIdAndDate(userId, targetDate)
                .orElseThrow(() -> new ResourceNotFoundException("Daily workout not found for date: " + targetDate));
    }

    public DailyWorkout findForDate(Long userId, LocalDate date) {
        LocalDate targetDate = date == null ? LocalDate.now() : date;
        return dailyWorkoutRepository.findByUser_UserIdAndDate(userId, targetDate).orElse(null);
    }

    public List<DailyWorkoutSet> getSets(Long userId, Long dailyWorkoutId) {
        DailyWorkout workout = dailyWorkoutRepository.findById(dailyWorkoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Daily workout not found with ID: " + dailyWorkoutId));
        if (!workout.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Daily workout does not belong to current user");
        }
        return dailyWorkoutSetRepository.findByDailyWorkout_DailyWorkoutId(dailyWorkoutId);
    }

    public DailyWorkoutSet addSet(Long userId, Long dailyWorkoutId, Long exerciseId) {
        DailyWorkout workout = dailyWorkoutRepository.findById(dailyWorkoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Daily workout not found with ID: " + dailyWorkoutId));
        if (!workout.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Daily workout does not belong to current user");
        }
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + exerciseId));

        int nextNumber = dailyWorkoutSetRepository.findByDailyWorkout_DailyWorkoutId(dailyWorkoutId)
                .stream()
                .filter(s -> s.getExercise() != null && s.getExercise().getExerciseId().equals(exerciseId))
                .mapToInt(DailyWorkoutSet::getSetNumber)
                .max()
                .orElse(0) + 1;

        DailyWorkoutSet set = new DailyWorkoutSet();
        set.setDailyWorkout(workout);
        set.setExercise(exercise);
        set.setSetNumber(nextNumber);
        return dailyWorkoutSetRepository.save(set);
    }

    public DailyWorkoutSet updateReps(Long userId, Long setId, Integer repsDone) {
        DailyWorkoutSet set = dailyWorkoutSetRepository.findWithWorkoutByDailyWorkoutSetId(setId)
                .orElseThrow(() -> new ResourceNotFoundException("Daily workout set not found with ID: " + setId));
        if (!set.getDailyWorkout().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Daily workout does not belong to current user");
        }
        set.setRepsDone(repsDone);
        return dailyWorkoutSetRepository.save(set);
    }

    public DailyWorkoutSet updateExercise(Long userId, Long setId, Long exerciseId) {
        DailyWorkoutSet set = dailyWorkoutSetRepository.findWithWorkoutByDailyWorkoutSetId(setId)
                .orElseThrow(() -> new ResourceNotFoundException("Daily workout set not found with ID: " + setId));
        if (!set.getDailyWorkout().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Daily workout does not belong to current user");
        }
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + exerciseId));
        set.setExercise(exercise);
        return dailyWorkoutSetRepository.save(set);
    }

    public void deleteSet(Long userId, Long setId) {
        DailyWorkoutSet set = dailyWorkoutSetRepository.findWithWorkoutByDailyWorkoutSetId(setId)
                .orElseThrow(() -> new ResourceNotFoundException("Daily workout set not found with ID: " + setId));
        if (!set.getDailyWorkout().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Daily workout does not belong to current user");
        }
        dailyWorkoutSetRepository.delete(set);
    }

    public void deleteWorkoutForDate(Long userId, LocalDate date) {
        LocalDate targetDate = date == null ? LocalDate.now() : date;
        DailyWorkout workout = dailyWorkoutRepository.findByUser_UserIdAndDate(userId, targetDate)
                .orElseThrow(() -> new ResourceNotFoundException("Daily workout not found for date: " + targetDate));
        if (!workout.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Daily workout does not belong to current user");
        }
        dailyWorkoutRepository.delete(workout);
    }
}
