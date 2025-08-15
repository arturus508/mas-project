package com.example.healthfitness.repository;

import com.example.healthfitness.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    Optional<HabitLog> findByHabitAndDate(Habit habit , LocalDate date);

    List<HabitLog> findByUserAndDateBetweenOrderByDateAsc(User user , LocalDate from , LocalDate to);

    List<HabitLog> findByHabitOrderByDateDesc(Habit habit);

    void deleteByHabit(Habit habit);
}
