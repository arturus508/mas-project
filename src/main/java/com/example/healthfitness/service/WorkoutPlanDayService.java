package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.Exercise;
import com.example.healthfitness.model.WorkoutPlan;
import com.example.healthfitness.model.WorkoutPlanDay;
import com.example.healthfitness.model.WorkoutPlanDayExercise;
import com.example.healthfitness.repository.ExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanDayExerciseRepository;
import com.example.healthfitness.repository.WorkoutPlanDayRepository;
import com.example.healthfitness.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutPlanDayService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanDayRepository workoutPlanDayRepository;
    private final WorkoutPlanDayExerciseRepository workoutPlanDayExerciseRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutPlanDayService(WorkoutPlanRepository workoutPlanRepository,
                                 WorkoutPlanDayRepository workoutPlanDayRepository,
                                 WorkoutPlanDayExerciseRepository workoutPlanDayExerciseRepository,
                                 ExerciseRepository exerciseRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanDayRepository = workoutPlanDayRepository;
        this.workoutPlanDayExerciseRepository = workoutPlanDayExerciseRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public List<WorkoutPlanDay> getPlanDaysForUser(Long userId, Long workoutPlanId) {
        WorkoutPlan plan = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with ID: " + workoutPlanId));
        if (!plan.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan does not belong to current user");
        }
        return workoutPlanDayRepository.findWithExercisesByWorkoutPlan_WorkoutPlanIdOrderByDayOrderAsc(workoutPlanId);
    }

    public List<WorkoutPlanDay> ensurePlanDays(Long userId, Long workoutPlanId, Integer daysPerWeek) {
        List<WorkoutPlanDay> days = getPlanDaysForUser(userId, workoutPlanId);
        if (!days.isEmpty()) {
            return days;
        }
        int count = daysPerWeek == null || daysPerWeek < 1 ? 1 : daysPerWeek;
        for (int i = 1; i <= count; i++) {
            createPlanDay(userId, workoutPlanId, "Day " + i, i);
        }
        return getPlanDaysForUser(userId, workoutPlanId);
    }

    public WorkoutPlanDay createPlanDay(Long userId, Long workoutPlanId, String name, Integer dayOrder) {
        WorkoutPlan plan = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with ID: " + workoutPlanId));
        if (!plan.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan does not belong to current user");
        }
        WorkoutPlanDay day = new WorkoutPlanDay();
        day.setWorkoutPlan(plan);
        day.setName(name == null || name.isBlank() ? "Day " + dayOrder : name);
        day.setDayOrder(dayOrder == null ? 1 : dayOrder);
        return workoutPlanDayRepository.save(day);
    }

    public WorkoutPlanDayExercise addExerciseToPlanDay(Long userId,
                                                       Long planDayId,
                                                       Long exerciseId,
                                                       Integer setsPlanned,
                                                       Integer targetReps,
                                                       Integer restTime) {
        WorkoutPlanDay day = workoutPlanDayRepository.findById(planDayId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan day not found with ID: " + planDayId));
        if (!day.getWorkoutPlan().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan does not belong to current user");
        }
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + exerciseId));
        WorkoutPlanDayExercise wpe = new WorkoutPlanDayExercise();
        wpe.setPlanDay(day);
        wpe.setExercise(exercise);
        wpe.setSetsPlanned(setsPlanned == null ? 1 : Math.max(1, setsPlanned));
        wpe.setTargetReps(targetReps);
        wpe.setRestTime(restTime);
        return workoutPlanDayExerciseRepository.save(wpe);
    }

    public void deletePlanDayExercise(Long userId, Long planDayExerciseId) {
        WorkoutPlanDayExercise exercise = workoutPlanDayExerciseRepository.findById(planDayExerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan day exercise not found with ID: " + planDayExerciseId));
        if (!exercise.getPlanDay().getWorkoutPlan().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Workout plan does not belong to current user");
        }
        workoutPlanDayExerciseRepository.delete(exercise);
    }
}
