package com.example.healthfitness.web.view;

public class TodayHabitItem {

    private Long id;
    private String name;
    private boolean done;
    private String cadence;
    private Integer progressDone;
    private Integer progressTarget;

    public TodayHabitItem() {}

    public TodayHabitItem(Long id, String name, boolean done) {
        this.id = id;
        this.name = name;
        this.done = done;
    }

    public TodayHabitItem(Long id, String name, boolean done, String cadence, Integer progressDone, Integer progressTarget) {
        this.id = id;
        this.name = name;
        this.done = done;
        this.cadence = cadence;
        this.progressDone = progressDone;
        this.progressTarget = progressTarget;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    public String getCadence() { return cadence; }
    public void setCadence(String cadence) { this.cadence = cadence; }

    public Integer getProgressDone() { return progressDone; }
    public void setProgressDone(Integer progressDone) { this.progressDone = progressDone; }

    public Integer getProgressTarget() { return progressTarget; }
    public void setProgressTarget(Integer progressTarget) { this.progressTarget = progressTarget; }
}
