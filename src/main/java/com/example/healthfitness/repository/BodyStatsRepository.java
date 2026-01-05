package com.example.healthfitness.repository;

import com.example.healthfitness.model.BodyStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BodyStatsRepository extends JpaRepository<BodyStats, Long> {
    /**
     * Find all BodyStats entries for a given user ordered by date recorded descending.
     * This avoids lazy initialization issues by fetching the body stats directly from the repository
     * instead of accessing the user's collection outside of a Hibernate session.
     *
     * @param userId the id of the user whose body stats should be retrieved
     * @return list of body stats sorted by most recent first
     */
    List<BodyStats> findByUserUserIdOrderByDateRecordedDesc(Long userId);
    List<BodyStats> findByUserUserIdAndDateRecorded(Long userId, LocalDate dateRecorded);
}
