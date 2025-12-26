package com.example.healthfitness.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "sleep_entry")
public class SleepEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDateTime sleepStart;

    @Column(nullable = false)
    private LocalDateTime sleepEnd;

    @Column(nullable = false)
    private Integer quality;   // 1..5

    @Column(length = 1000)
    private String note;

    public SleepEntry() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalDateTime getSleepStart() { return sleepStart; }
    public void setSleepStart(LocalDateTime sleepStart) { this.sleepStart = sleepStart; }

    public LocalDateTime getSleepEnd() { return sleepEnd; }
    public void setSleepEnd(LocalDateTime sleepEnd) { this.sleepEnd = sleepEnd; }

    public Integer getQuality() { return quality; }
    public void setQuality(Integer quality) { this.quality = quality; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @Transient
    public long getDurationMinutes() {
        if (sleepStart == null || sleepEnd == null) return 0;
        LocalDateTime end = sleepEnd;
        if (end.isBefore(sleepStart)) end = end.plusDays(1);
        return Duration.between(sleepStart, end).toMinutes();
    }

    @Transient
    public String getDurationText() {
        long minutes = getDurationMinutes();
        long h = minutes / 60;
        long m = minutes % 60;
        return h + "h " + m + "m";
    }
}
