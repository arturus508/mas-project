package com.example.healthfitness.repository;

import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SleepEntryRepository extends JpaRepository<SleepEntry, Long> {
    List<SleepEntry> findByUserAndDateBetweenOrderByDateAsc(User user, LocalDate start, LocalDate end);
    Optional<SleepEntry> findByUserAndDate(User user, LocalDate date);
}
