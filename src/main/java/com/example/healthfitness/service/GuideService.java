package com.example.healthfitness.service;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private GuideReadRepository guideReadRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Guide> listByCategory(GuideCategory category) {
        return guideRepository.findAllByCategoryAndIsPublishedTrueOrderByCreatedAtDesc(category);
    }

    public Guide getBySlug(String slug) {
        return guideRepository.findBySlugAndIsPublishedTrue(slug)
                .orElseThrow(() -> new NoSuchElementException("Guide not found: " + slug));
    }

    public void touchLastViewed(Long guideId, Long userId) {
        var user  = userRepository.findById(userId).orElseThrow();
        var guide = guideRepository.findById(guideId).orElseThrow();
        var gr    = guideReadRepository.findByUserAndGuide(user, guide).orElseGet(() -> {
            var x = new GuideRead();
            x.setUser(user); x.setGuide(guide);
            return x;
        });
        gr.setLastViewedAt(LocalDateTime.now());
        if (gr.getProgressPct() == null) gr.setProgressPct(0);
        guideReadRepository.save(gr);
    }

    public void markCompleted(Long guideId, Long userId) {
        var user  = userRepository.findById(userId).orElseThrow();
        var guide = guideRepository.findById(guideId).orElseThrow();
        var gr    = guideReadRepository.findByUserAndGuide(user, guide).orElseGet(() -> {
            var x = new GuideRead();
            x.setUser(user); x.setGuide(guide);
            return x;
        });
        gr.setCompletedAt(LocalDateTime.now());
        gr.setProgressPct(100);
        gr.setLastViewedAt(LocalDateTime.now());
        guideReadRepository.save(gr);
    }

    public List<Guide> recommendMindGuides(Long userId, int limit) {
        var user = userRepository.findById(userId).orElseThrow();
        var rec = guideRepository.findTop5ByCategoryAndIsPublishedTrueOrderByEstReadingMinAsc(GuideCategory.MIND);
        if (rec.size() > limit) return rec.subList(0, limit);
        return rec;
    }
}
