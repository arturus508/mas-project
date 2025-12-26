package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.repository.SleepEntryRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.form.SleepForm;
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
        var user   = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        var start  = ym.atDay(1);
        var end    = ym.atEndOfMonth();
        return sleepEntryRepository.findByUserAndDateBetweenOrderByDateAsc(user, start, end);
    }

    public Optional<SleepEntry> getYesterday(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        var y    = LocalDate.now().minusDays(1);
        return sleepEntryRepository.findByUserAndDate(user, y);
    }

    public Optional<SleepEntry> getByDate(Long userId, LocalDate date) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return sleepEntryRepository.findByUserAndDate(user, date);
    }

    public SleepEntry save(Long userId, SleepEntry input) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        input.setUser(user);
        if (input.getDate() == null) input.setDate(input.getSleepStart() != null ? input.getSleepStart().toLocalDate() : LocalDate.now());
        return sleepEntryRepository.save(input);
    }

    public SleepEntry create(Long userId, SleepForm form) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        LocalDate date = form.getDate();
        int hours = form.getHours();
        int minutes = form.getMinutes();
        LocalDateTime start = date.atTime(22, 0);
        LocalDateTime end = start.plusHours(hours).plusMinutes(minutes);

        SleepEntry entry = new SleepEntry();
        entry.setUser(user);
        entry.setDate(date);
        entry.setSleepStart(start);
        entry.setSleepEnd(end);
        entry.setQuality(form.getQuality());
        entry.setNote(form.getNote());
        return sleepEntryRepository.save(entry);
    }

    public void delete(Long userId, Long entryId) {
        SleepEntry entry = sleepEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Sleep entry not found with id: " + entryId));
        if (!entry.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Sleep entry does not belong to current user");
        }
        sleepEntryRepository.delete(entry);
    }

    public long durationMinutes(SleepEntry e) {
        var start = e.getSleepStart();
        var end   = e.getSleepEnd();
        if (end.isBefore(start)) end = end.plusDays(1);
        return Duration.between(start, end).toMinutes();
    }
}
