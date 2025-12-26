package com.example.healthfitness.repository;

import com.example.healthfitness.model.DailyWorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyWorkoutSetRepository extends JpaRepository<DailyWorkoutSet, Long> {
    List<DailyWorkoutSet> findByDailyWorkout_DailyWorkoutId(Long dailyWorkoutId);
}
