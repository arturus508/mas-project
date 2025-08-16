package com.example.healthfitness.repository;

import com.example.healthfitness.model.Task;
import com.example.healthfitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserAndDateOrderByIdAsc(User user, LocalDate date);
    List<Task> findByUserAndDateBetweenOrderByDateAsc(User user, LocalDate start, LocalDate end);

    @Query("""
           select count(t) from Task t
           where t.user = :user
             and t.done = true
             and t.date between :start and :end
           """)
    long countCompletedInRange(@Param("user") User user,
                               @Param("start") LocalDate start,
                               @Param("end") LocalDate end);
}
