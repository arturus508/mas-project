package com.example.healthfitness.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "weekly_goal")
public class WeeklyGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false , length = 160)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 12)
    private WeeklyGoalType type;

    @Column(nullable = false)
    private LocalDate weekStart;

    @Column(nullable = false)
    private LocalDate weekEnd;

    @Column(nullable = false)
    private Integer targetValue;

    @Column(nullable = false)
    private Integer currentValue = 0;

    private boolean   completed = false;
    private LocalDate completedDate;

    public WeeklyGoal() {}

    public Long getId() { return id; }                            public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }                        public void setUser(User user) { this.user = user; }
    public String getTitle() { return title; }                    public void setTitle(String title) { this.title = title; }
    public WeeklyGoalType getType() { return type; }              public void setType(WeeklyGoalType type) { this.type = type; }
    public LocalDate getWeekStart() { return weekStart; }         public void setWeekStart(LocalDate weekStart) { this.weekStart = weekStart; }
    public LocalDate getWeekEnd() { return weekEnd; }             public void setWeekEnd(LocalDate weekEnd) { this.weekEnd = weekEnd; }
    public Integer getTargetValue() { return targetValue; }       public void setTargetValue(Integer targetValue) { this.targetValue = targetValue; }
    public Integer getCurrentValue() { return currentValue; }     public void setCurrentValue(Integer currentValue) { this.currentValue = currentValue; }
    public boolean isCompleted() { return completed; }            public void setCompleted(boolean completed) { this.completed = completed; }
    public LocalDate getCompletedDate() { return completedDate; } public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }
}
