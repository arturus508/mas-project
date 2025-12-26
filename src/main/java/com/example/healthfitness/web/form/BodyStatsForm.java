package com.example.healthfitness.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Form backing bean for creating or editing body statistics.  This DTO
 * decouples the web layer from the persistence model and allows
 * validation rules to be applied on user input prior to mapping into
 * the {@link com.example.healthfitness.model.BodyStats} entity.
 */
public class BodyStatsForm {

    /**
     * The date the body measurements were recorded.  A value is
     * required and bound using ISO‑8601 format.  See
     * {@link DateTimeFormat#iso()} for details.
     */
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateRecorded;

    /** The weight in kilograms.  Must be non‑negative. */
    @Min(value = 0, message = "Weight cannot be negative")
    private Double weight;

    /**
     * The body fat percentage.  Must be non‑negative.  Depending on
     * your domain this could be constrained to 0–100 but here we
     * simply require a non‑negative value.
     */
    @Min(value = 0, message = "Body fat percentage cannot be negative")
    private Double bodyFatPercent;

    public LocalDate getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(LocalDate dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBodyFatPercent() {
        return bodyFatPercent;
    }

    public void setBodyFatPercent(Double bodyFatPercent) {
        this.bodyFatPercent = bodyFatPercent;
    }
}