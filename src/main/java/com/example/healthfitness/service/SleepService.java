package com.example.healthfitness.service;

import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.SleepEntryRepository;
import com.example.healthfitness.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class SleepService {

    @Autowired
    private SleepEntryRepository sleepEntryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SleepEntry> listForMonth(Long userId, YearMonth ym) {
        var user   = userRepository.findById(userId).orElseThrow();
        var start  = ym.atDay(1);
        var end    = ym.atEndOfMonth();
        return sleepEntryRepository.findByUserAndDateBetweenOrderByDateAsc(user, start, end);
    }

    public Optional<SleepEntry> getYesterday(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        var y    = LocalDate.now().minusDays(1);
        return sleepEntryRepository.findByUserAndDate(user, y);
    }

    public SleepEntry save(Long userId, SleepEntry input) {
        var user = userRepository.findById(userId).orElseThrow();
        input.setUser(user);
        if (input.getDate() == null) input.setDate(input.getSleepStart() != null ? input.getSleepStart().toLocalDate() : LocalDate.now());
        return sleepEntryRepository.save(input);
    }

    public long durationMinutes(SleepEntry e) {
        var start = e.getSleepStart();
        var end   = e.getSleepEnd();
        if (end.isBefore(start)) end = end.plusDays(1);
        return Duration.between(start, end).toMinutes();
    }
}
