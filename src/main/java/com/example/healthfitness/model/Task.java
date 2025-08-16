package com.example.healthfitness.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false , length = 200)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean done = false;

    private LocalDate completedDate;

    public Task() {}

    public Long getId() { return id; }                     public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }                 public void setUser(User user) { this.user = user; }
    public String getTitle() { return title; }             public void setTitle(String title) { this.title = title; }
    public LocalDate getDate() { return date; }            public void setDate(LocalDate date) { this.date = date; }
    public boolean isDone() { return done; }               public void setDone(boolean done) { this.done = done; }
    public LocalDate getCompletedDate() { return completedDate; } public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }
}
