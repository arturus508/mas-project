package com.example.healthfitness.web.view;

public class HiddenSectionItem {
    private final String key;
    private final String label;

    public HiddenSectionItem(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String getKey() { return key; }
    public String getLabel() { return label; }
}
