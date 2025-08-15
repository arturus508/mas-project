package com.example.healthfitness.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "dream_entry")
public class DreamEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sleep_entry_id")
    private SleepEntry sleepEntry;      // optional link  (can be null)

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(length = 255)
    private String tags;                // comma separated

    private Integer mood;               // 1..5
    private Boolean lucid;
    private Boolean nightmare;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public DreamEntry() {}

    @PrePersist
    void t() { this.createdAt = LocalDateTime.now();  if (this.date == null) this.date = LocalDate.now(); }

    public Long getId() { return id; }                 public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }             public void setUser(User user) { this.user = user; }
    public SleepEntry getSleepEntry() { return sleepEntry; }   public void setSleepEntry(SleepEntry sleepEntry) { this.sleepEntry = sleepEntry; }
    public LocalDate getDate() { return date; }        public void setDate(LocalDate date) { this.date = date; }
    public String getTitle() { return title; }         public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }     public void setContent(String content) { this.content = content; }
    public String getTags() { return tags; }           public void setTags(String tags) { this.tags = tags; }
    public Integer getMood() { return mood; }          public void setMood(Integer mood) { this.mood = mood; }
    public Boolean getLucid() { return lucid; }        public void setLucid(Boolean lucid) { this.lucid = lucid; }
    public Boolean getNightmare() { return nightmare; }public void setNightmare(Boolean nightmare) { this.nightmare = nightmare; }
    public LocalDateTime getCreatedAt() { return createdAt; }  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
