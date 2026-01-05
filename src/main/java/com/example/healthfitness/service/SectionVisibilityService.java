package com.example.healthfitness.service;

import com.example.healthfitness.model.User;
import com.example.healthfitness.model.UserSectionVisibility;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.repository.UserSectionVisibilityRepository;
import com.example.healthfitness.web.view.SectionVisibilityItem;
import com.example.healthfitness.web.view.HiddenSectionItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SectionVisibilityService {

    private final UserSectionVisibilityRepository repository;
    private final UserRepository userRepository;

    private static final Map<String, SectionMeta> SECTION_META = new LinkedHashMap<>();

    static {
        SECTION_META.put("today-workout", new SectionMeta("Workout", "body", "today"));
        SECTION_META.put("today-meals", new SectionMeta("Meals", "body", "today"));
        SECTION_META.put("today-body-stats", new SectionMeta("Body stats", "body", "today"));

        SECTION_META.put("today-tasks", new SectionMeta("Tasks", "mind", "today"));
        SECTION_META.put("today-habits", new SectionMeta("Habits", "mind", "today"));
        SECTION_META.put("today-sleep", new SectionMeta("Sleep", "mind", "today"));
        SECTION_META.put("today-review", new SectionMeta("Review", "mind", "today"));

        SECTION_META.put("week-nutrition", new SectionMeta("Nutrition", "body", "week"));
        SECTION_META.put("week-workouts", new SectionMeta("Workouts", "body", "week"));
        SECTION_META.put("week-weight", new SectionMeta("Weight trend", "body", "week"));

        SECTION_META.put("week-sleep", new SectionMeta("Sleep", "mind", "week"));
        SECTION_META.put("week-tasks-habits", new SectionMeta("Tasks & Habits", "mind", "week"));
        SECTION_META.put("week-review", new SectionMeta("Review", "mind", "week"));
    }

    public SectionVisibilityService(UserSectionVisibilityRepository repository,
                                    UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Map<String, Boolean> getHiddenMap(Long userId) {
        Map<String, Boolean> hidden = new LinkedHashMap<>();
        for (UserSectionVisibility vis : repository.findByUser_UserId(userId)) {
            if (vis.isHidden()) {
                hidden.put(vis.getSectionKey(), true);
            }
        }
        return hidden;
    }

    public List<HiddenSectionItem> getHiddenItemsFor(String mode, String range, Set<String> keys) {
        List<HiddenSectionItem> items = new ArrayList<>();
        for (String key : keys) {
            SectionMeta meta = SECTION_META.get(key);
            if (meta == null) {
                continue;
            }
            if (!meta.mode.equals(mode) || !meta.range.equals(range)) {
                continue;
            }
            items.add(new HiddenSectionItem(key, meta.label));
        }
        return items;
    }

    public List<SectionVisibilityItem> getSections(Long userId) {
        Map<String, Boolean> hiddenMap = getHiddenMap(userId);
        List<SectionVisibilityItem> items = new ArrayList<>();
        for (Map.Entry<String, SectionMeta> entry : SECTION_META.entrySet()) {
            String key = entry.getKey();
            SectionMeta meta = entry.getValue();
            boolean hidden = hiddenMap.getOrDefault(key, false);
            items.add(new SectionVisibilityItem(key, meta.label, meta.mode, meta.range, hidden));
        }
        return items;
    }

    public void setHidden(Long userId, String sectionKey, boolean hidden) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.example.healthfitness.exception.ResourceNotFoundException("User not found with id: " + userId));
        if (!hidden) {
            repository.findByUser_UserIdAndSectionKey(userId, sectionKey)
                    .ifPresent(repository::delete);
            return;
        }
        UserSectionVisibility vis = repository.findByUser_UserIdAndSectionKey(userId, sectionKey)
                .orElseGet(UserSectionVisibility::new);
        vis.setUser(user);
        vis.setSectionKey(sectionKey);
        vis.setHidden(true);
        repository.save(vis);
    }

    private static class SectionMeta {
        private final String label;
        private final String mode;
        private final String range;

        private SectionMeta(String label, String mode, String range) {
            this.label = label;
            this.mode = mode;
            this.range = range;
        }
    }
}
