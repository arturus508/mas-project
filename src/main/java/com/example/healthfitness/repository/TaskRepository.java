package com.example.healthfitness.repository;

import com.example.healthfitness.model.Task;
import com.example.healthfitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserAndDateOrderByIdAsc(User user , LocalDate date);
}
