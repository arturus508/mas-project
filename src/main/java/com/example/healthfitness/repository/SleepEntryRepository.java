package com.example.healthfitness.repository;

import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface SleepEntryRepository extends JpaRepository<SleepEntry, Long> {
    Optional<SleepEntry> findByUserAndDate(User user, LocalDate date);
    List<SleepEntry> findByUserAndDateBetweenOrderByDateAsc(User user, LocalDate start, LocalDate end);
}
