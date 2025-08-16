package com.example.healthfitness.repository;

import com.example.healthfitness.model.User;
import com.example.healthfitness.model.WeeklyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyGoalRepository extends JpaRepository<WeeklyGoal, Long> {

    List<WeeklyGoal> findByUserOrderByWeekStartDesc(User user);

    List<WeeklyGoal> findByUserAndWeekStartLessThanEqualAndWeekEndGreaterThanEqual(
            User user , LocalDate end , LocalDate start);
}
