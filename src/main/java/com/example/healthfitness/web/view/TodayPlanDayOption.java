package com.example.healthfitness.web.view;

public class TodayPlanDayOption {
    private final Long id;
    private final String label;

    public TodayPlanDayOption(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public Long getId() { return id; }

    public String getLabel() { return label; }
}
