package com.example.healthfitness.repository;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUserOrderByIdAsc(User user);
    List<Habit> findByUserAndActiveTrueOrderByIdAsc(User user);
}
