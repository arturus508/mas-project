package com.example.healthfitness.web.form;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Form backing object for recording body statistics.  Using BigDecimal
 * for numeric fields provides better precision and integrates well
 * with validation annotations.  After binding and validation the
 * numeric values can be converted to doubles for storage.
 */
public class BodyStatsForm {

    @NotNull
    private LocalDate date;

    @NotNull
    @DecimalMin(value = "1.0", inclusive = false)
    private BigDecimal weightKg;

    private BigDecimal bodyFatPercent;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }

    public BigDecimal getBodyFatPercent() { return bodyFatPercent; }
    public void setBodyFatPercent(BigDecimal bodyFatPercent) { this.bodyFatPercent = bodyFatPercent; }
}