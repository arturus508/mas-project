package com.example.healthfitness.service;

import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.DailyReview;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.DailyReviewRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.form.DailyReviewForm;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DailyReviewService {

    private final DailyReviewRepository dailyReviewRepository;
    private final UserRepository userRepository;

    public DailyReviewService(DailyReviewRepository dailyReviewRepository,
                              UserRepository userRepository) {
        this.dailyReviewRepository = dailyReviewRepository;
        this.userRepository = userRepository;
    }

    public Optional<DailyReview> findForDate(Long userId, LocalDate date) {
        LocalDate targetDate = date == null ? LocalDate.now() : date;
        return dailyReviewRepository.findByUser_UserIdAndDate(userId, targetDate);
    }

    public DailyReview saveReview(Long userId, LocalDate date, DailyReviewForm form) {
        LocalDate targetDate = date == null ? LocalDate.now() : date;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        DailyReview review = dailyReviewRepository.findByUser_UserIdAndDate(userId, targetDate)
                .orElseGet(() -> {
                    DailyReview fresh = new DailyReview();
                    fresh.setUser(user);
                    fresh.setDate(targetDate);
                    return fresh;
                });

        review.setMood(form.getMood());
        review.setEnergy(form.getEnergy());
        String note = form.getNote() == null ? null : form.getNote().trim();
        review.setNote(note == null || note.isEmpty() ? null : note);
        review.setResetDone(form.isResetDone());
        return dailyReviewRepository.save(review);
    }
}
