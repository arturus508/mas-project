package com.example.healthfitness.repository;

import com.example.healthfitness.model.DailyWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyWorkoutRepository extends JpaRepository<DailyWorkout, Long> {
    Optional<DailyWorkout> findByUser_UserIdAndDate(Long userId, LocalDate date);
    List<DailyWorkout> findByUser_UserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}
