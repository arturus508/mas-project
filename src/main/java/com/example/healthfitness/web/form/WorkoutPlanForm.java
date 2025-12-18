package com.example.healthfitness.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Form backing object for creating or editing a workout plan.  The
 * entity {@link com.example.healthfitness.model.WorkoutPlan} has
 * additional fields such as status and days per week, but these can
 * be defaulted or left to a later editing interface.  Validation
 * ensures a name and start date are provided.
 */
public class WorkoutPlanForm {

    @NotBlank
    private String planName;

    private String description;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private Integer daysPerWeek;

    // getters and setters
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getDaysPerWeek() { return daysPerWeek; }
    public void setDaysPerWeek(Integer daysPerWeek) { this.daysPerWeek = daysPerWeek; }
}