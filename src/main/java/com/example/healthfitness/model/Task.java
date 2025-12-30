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

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private LocalDate date;

    private LocalDate dueDate;

    @Column(nullable = false)
    private boolean done = false;

    @Column(name = "is_top", nullable = false)
    private boolean top = false;

    private LocalDate completedDate;

    @Column(length = 16)
    private String priority = "MEDIUM";

    public Task() {}

    public Long getId() { return id; }                     public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }                 public void setUser(User user) { this.user = user; }
    public String getTitle() { return title; }             public void setTitle(String title) { this.title = title; }
    public String getNotes() { return notes; }             public void setNotes(String notes) { this.notes = notes; }
    public LocalDate getDate() { return date; }            public void setDate(LocalDate date) { this.date = date; }
    public LocalDate getDueDate() { return dueDate; }      public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public boolean isDone() { return done; }               public void setDone(boolean done) { this.done = done; }
    public boolean isTop() { return top; }                 public void setTop(boolean top) { this.top = top; }
    public LocalDate getCompletedDate() { return completedDate; } public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }
    public String getPriority() { return priority; }       public void setPriority(String priority) { this.priority = priority; }
}
