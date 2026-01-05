package com.example.healthfitness.web.view;

public class SectionVisibilityItem {
    private final String key;
    private final String label;
    private final String mode;
    private final String range;
    private final boolean hidden;

    public SectionVisibilityItem(String key, String label, String mode, String range, boolean hidden) {
        this.key = key;
        this.label = label;
        this.mode = mode;
        this.range = range;
        this.hidden = hidden;
    }

    public String getKey() { return key; }

    public String getLabel() { return label; }

    public String getMode() { return mode; }

    public String getRange() { return range; }

    public boolean isHidden() { return hidden; }
}
