package com.example.healthfitness.repository;

import com.example.healthfitness.model.DailyWorkout;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyWorkoutRepository extends JpaRepository<DailyWorkout, Long> {
    Optional<DailyWorkout> findByUser_UserIdAndDate(Long userId, LocalDate date);
    List<DailyWorkout> findByUser_UserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    @Modifying
    @Query("""
           update DailyWorkout dw
           set dw.planDay = null
           where dw.planDay.planDayId in (
             select d.planDayId from WorkoutPlanDay d
             where d.workoutPlan.workoutPlanId = :planId
           )
           """)
    int clearPlanDayForWorkoutPlan(@Param("planId") Long planId);
}
