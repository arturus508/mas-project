package com.example.healthfitness.repository;

import com.example.healthfitness.model.DailyWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyWorkoutRepository extends JpaRepository<DailyWorkout, Long> {
    Optional<DailyWorkout> findByUser_UserIdAndDate(Long userId, LocalDate date);
}
