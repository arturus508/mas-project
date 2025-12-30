package com.example.healthfitness.repository;

import com.example.healthfitness.model.DailyWorkoutSet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyWorkoutSetRepository extends JpaRepository<DailyWorkoutSet, Long> {
    @EntityGraph(attributePaths = {"exercise"})
    List<DailyWorkoutSet> findByDailyWorkout_DailyWorkoutId(Long dailyWorkoutId);

    @EntityGraph(attributePaths = {"dailyWorkout", "dailyWorkout.user"})
    java.util.Optional<DailyWorkoutSet> findWithWorkoutByDailyWorkoutSetId(Long dailyWorkoutSetId);

    @Query("""
           select s from DailyWorkoutSet s
           join s.dailyWorkout dw
           left join fetch s.exercise
           where dw.user.userId = :userId
             and dw.date between :start and :end
           """)
    List<DailyWorkoutSet> findByUserAndDateBetweenWithExercise(@Param("userId") Long userId,
                                                               @Param("start") java.time.LocalDate start,
                                                               @Param("end") java.time.LocalDate end);
}
