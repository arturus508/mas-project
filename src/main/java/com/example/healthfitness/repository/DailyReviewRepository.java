package com.example.healthfitness.repository;

import com.example.healthfitness.model.DailyReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyReviewRepository extends JpaRepository<DailyReview, Long> {
    Optional<DailyReview> findByUser_UserIdAndDate(Long userId, LocalDate date);
    List<DailyReview> findByUser_UserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}
