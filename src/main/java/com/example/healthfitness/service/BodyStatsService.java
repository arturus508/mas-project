package com.example.healthfitness.service;
import com.example.healthfitness.model.User;
import com.example.healthfitness.model.BodyStats;

import com.example.healthfitness.repository.BodyStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.healthfitness.repository.UserRepository;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class BodyStatsService {


    @Autowired
    private BodyStatsRepository bodyStatsRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BodyStats> getAllBodyStats() {
        return bodyStatsRepository.findAll();
    }

    public BodyStats saveBodyStats(BodyStats bodyStats) {
        return bodyStatsRepository.save(bodyStats);
    }
    public List<BodyStats> getBodyStatsByUser(Long userId) {
        // Fetch body stats directly from repository to avoid LazyInitializationException. Fetching via user.getBodyStats() would require an active Hibernate session.
        return bodyStatsRepository.findByUserUserIdOrderByDateRecordedDesc(userId);
    }

    public void addBodyStatsToUser(Long userId, BodyStats bodyStats) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        bodyStats.setUser(user);
        bodyStatsRepository.save(bodyStats);
    }

    public void deleteBodyStats(Long id) {
        bodyStatsRepository.deleteById(id);
    }



}
