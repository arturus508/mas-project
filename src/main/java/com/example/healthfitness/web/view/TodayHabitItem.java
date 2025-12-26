package com.example.healthfitness.web.view;

public class TodayHabitItem {

    private Long id;
    private String name;
    private boolean done;

    public TodayHabitItem() {}

    public TodayHabitItem(Long id, String name, boolean done) {
        this.id = id;
        this.name = name;
        this.done = done;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
}
