package com.example.healthfitness.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "habit_log",
       uniqueConstraints = @UniqueConstraint(columnNames = {"habit_id","date"}))
public class HabitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean done;

    private Integer value;     // optional numeric value

    public HabitLog(){}

    public Long getId(){ return id; }           public void setId(Long id){ this.id = id; }
    public Habit getHabit(){ return habit; }    public void setHabit(Habit habit){ this.habit = habit; }
    public User getUser(){ return user; }       public void setUser(User user){ this.user = user; }
    public LocalDate getDate(){ return date; }  public void setDate(LocalDate date){ this.date = date; }
    public Boolean getDone(){ return done; }    public void setDone(Boolean done){ this.done = done; }
    public Integer getValue(){ return value; }  public void setValue(Integer value){ this.value = value; }
}
