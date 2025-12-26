package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.*;
import com.example.healthfitness.web.form.DreamForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DreamService {

    @Autowired
    private DreamEntryRepository dreamEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SleepEntryRepository sleepEntryRepository;

    public DreamEntry create(Long userId, DreamForm form) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        var d = new DreamEntry();
        d.setUser(user);
        if (form.getSleepEntryId() != null) {
            var se = sleepEntryRepository.findById(form.getSleepEntryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sleep entry not found with id: " + form.getSleepEntryId()));
            if (!se.getUser().getUserId().equals(userId)) {
                throw new ForbiddenException("Sleep entry does not belong to current user");
            }
            d.setSleepEntry(se);
        }
        d.setDate(form.getDate());
        d.setTitle(form.getTitle());
        d.setContent(form.getContent());
        d.setTags(form.getTags());
        d.setMood(form.getMood());
        d.setLucid(Boolean.TRUE.equals(form.getLucid()));
        d.setNightmare(Boolean.TRUE.equals(form.getNightmare()));
        return dreamEntryRepository.save(d);
    }

    public void delete(Long userId, Long dreamId) {
        DreamEntry entry = dreamEntryRepository.findById(dreamId)
                .orElseThrow(() -> new ResourceNotFoundException("Dream not found with id: " + dreamId));
        if (!entry.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Dream does not belong to current user");
        }
        dreamEntryRepository.delete(entry);
    }

    public List<DreamEntry> listAll(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return dreamEntryRepository.findByUserOrderByDateDesc(user);
    }

    public List<DreamEntry> search(Long userId, LocalDate from, LocalDate to, String tag) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (tag != null && !tag.isBlank()) return dreamEntryRepository.findByUserAndTagsContainingIgnoreCaseOrderByDateDesc(user, tag);
        if (from != null && to != null)     return dreamEntryRepository.findByUserAndDateBetweenOrderByDateDesc(user, from, to);
        return dreamEntryRepository.findByUserOrderByDateDesc(user);
    }
}
