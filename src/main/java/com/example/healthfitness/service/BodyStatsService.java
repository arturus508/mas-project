package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.BodyStats;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.BodyStatsRepository;
import com.example.healthfitness.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for reading and writing {@link BodyStats} entities.
 * This service encapsulates persistence access and mapping from
 * incoming DTOs to entities.  Controllers should delegate all
 * business logic to this service rather than interacting with
 * repositories directly.
 */
@Service
public class BodyStatsService {

    @Autowired
    private BodyStatsRepository bodyStatsRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Return all body stats in the system.  Typically used only for
     * debugging or administrative views.
     */
    public List<BodyStats> getAllBodyStats() {
        return bodyStatsRepository.findAll();
    }

    /**
     * Persist the given BodyStats entity.  Prefer using
     * {@link #addBodyStatsToUser(Long, com.example.healthfitness.web.form.BodyStatsForm)}
     * to create a new stats record bound to a user.
     */
    public BodyStats saveBodyStats(BodyStats bodyStats) {
        return bodyStatsRepository.save(bodyStats);
    }

    /**
     * Retrieve all body stats belonging to a given user, ordered by
     * descending date.
     *
     * @param userId id of the user whose stats to retrieve
     * @return list of body stats for the user
     */
    public List<BodyStats> getBodyStatsByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return bodyStatsRepository.findByUserUserIdOrderByDateRecordedDesc(userId);
    }

    /**
     * Persist an existing BodyStats entity and associate it with the
     * specified user.  Prefer using the overload that accepts a form
     * object when creating new stats from user input.
     */
    public BodyStats addBodyStatsToUser(Long userId, BodyStats bodyStats) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        bodyStats.setUser(user);
        return bodyStatsRepository.save(bodyStats);
    }

    /**
     * Create and persist a {@link BodyStats} entity for the given user
     * from the values contained in the provided form.  This method
     * encapsulates the mapping from DTO to entity and associates the
     * new BodyStats with the given user.  A runtime exception is
     * thrown if the user id is not found.
     *
     * @param userId the id of the user to whom the stats belong
     * @param form   the data transfer object containing the new
     *               measurements
     */
    public void addBodyStatsToUser(Long userId, com.example.healthfitness.web.form.BodyStatsForm form) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        BodyStats stats = new BodyStats(form.getDateRecorded(), form.getWeight(), form.getBodyFatPercent());
        stats.setUser(user);
        bodyStatsRepository.save(stats);
    }

    /**
     * Delete a BodyStats entity by id.  If the id does not exist the
     * repository silently ignores the operation.
     *
     * @param id the id of the BodyStats record to remove
     */
    public void deleteBodyStats(Long id) {
        BodyStats stats = bodyStatsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Body stats not found with id: " + id));
        bodyStatsRepository.delete(stats);
    }

    public void deleteBodyStats(Long userId, Long id) {
        BodyStats stats = bodyStatsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Body stats not found with id: " + id));
        if (stats.getUser() == null || !stats.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Body stats do not belong to current user");
        }
        bodyStatsRepository.delete(stats);
    }
}
