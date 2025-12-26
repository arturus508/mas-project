package com.example.healthfitness.web.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Form backing object for creating sleep entries.
 */
public class SleepForm {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull
    @Min(0)
    @Max(24)
    private Integer hours;

    @NotNull
    @Min(0)
    @Max(59)
    private Integer minutes;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer quality;

    private String note;

    @AssertTrue(message = "Duration must be at least 1 minute")
    public boolean isDurationValid() {
        int h = hours == null ? 0 : hours;
        int m = minutes == null ? 0 : minutes;
        return (h * 60 + m) > 0;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getHours() { return hours; }
    public void setHours(Integer hours) { this.hours = hours; }

    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer minutes) { this.minutes = minutes; }

    public Integer getQuality() { return quality; }
    public void setQuality(Integer quality) { this.quality = quality; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
