package com.example.healthfitness.repository;

import com.example.healthfitness.model.BodyStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyStatsRepository extends JpaRepository<BodyStats, Long> {
}
