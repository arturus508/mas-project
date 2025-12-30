package com.example.healthfitness.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "daily_review",
       uniqueConstraints = @UniqueConstraint(name = "uk_daily_review_user_date", columnNames = {"user_id", "date"}))
public class DailyReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private Integer mood;

    @Column
    private Integer energy;

    @Column(length = 1000)
    private String note;

    @Column(name = "reset_done", nullable = false)
    private boolean resetDone;

    public Long getDailyReviewId() { return dailyReviewId; }
    public void setDailyReviewId(Long dailyReviewId) { this.dailyReviewId = dailyReviewId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getMood() { return mood; }
    public void setMood(Integer mood) { this.mood = mood; }

    public Integer getEnergy() { return energy; }
    public void setEnergy(Integer energy) { this.energy = energy; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public boolean isResetDone() { return resetDone; }
    public void setResetDone(boolean resetDone) { this.resetDone = resetDone; }
}
