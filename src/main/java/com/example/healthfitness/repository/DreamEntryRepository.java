package com.example.healthfitness.repository;

import com.example.healthfitness.model.DreamEntry;
import com.example.healthfitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface DreamEntryRepository extends JpaRepository<DreamEntry, Long> {
    List<DreamEntry> findByUserOrderByDateDesc(User user);
    List<DreamEntry> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate from, LocalDate to);
    List<DreamEntry> findByUserAndTagsContainingIgnoreCaseOrderByDateDesc(User user, String tag);
}
