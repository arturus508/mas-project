package com.example.healthfitness.web.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class DailyReviewForm {

    @Min(1)
    @Max(5)
    private Integer mood;

    @Min(1)
    @Max(5)
    private Integer energy;

    private String note;

    private boolean resetDone;

    public Integer getMood() { return mood; }
    public void setMood(Integer mood) { this.mood = mood; }

    public Integer getEnergy() { return energy; }
    public void setEnergy(Integer energy) { this.energy = energy; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public boolean isResetDone() { return resetDone; }
    public void setResetDone(boolean resetDone) { this.resetDone = resetDone; }
}
