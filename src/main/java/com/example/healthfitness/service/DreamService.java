package com.example.healthfitness.service;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.*;
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

    public DreamEntry add(Long userId, Long sleepEntryId, String title, String content,
                          String tags, Integer mood, Boolean lucid, Boolean nightmare, LocalDate date) {
        var user = userRepository.findById(userId).orElseThrow();
        var d = new DreamEntry();
        d.setUser(user);
        if (sleepEntryId != null) {
            var se = sleepEntryRepository.findById(sleepEntryId).orElse(null);
            d.setSleepEntry(se);
            if (date == null && se != null) date = se.getDate();
        }
        d.setDate(date != null ? date : LocalDate.now());
        d.setTitle(title);
        d.setContent(content);
        d.setTags(tags);
        d.setMood(mood);
        d.setLucid(Boolean.TRUE.equals(lucid));
        d.setNightmare(Boolean.TRUE.equals(nightmare));
        return dreamEntryRepository.save(d);
    }

    public List<DreamEntry> listAll(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        return dreamEntryRepository.findByUserOrderByDateDesc(user);
    }

    public List<DreamEntry> search(Long userId, LocalDate from, LocalDate to, String tag) {
        var user = userRepository.findById(userId).orElseThrow();
        if (tag != null && !tag.isBlank()) return dreamEntryRepository.findByUserAndTagsContainingIgnoreCaseOrderByDateDesc(user, tag);
        if (from != null && to != null)     return dreamEntryRepository.findByUserAndDateBetweenOrderByDateDesc(user, from, to);
        return dreamEntryRepository.findByUserOrderByDateDesc(user);
    }
}
