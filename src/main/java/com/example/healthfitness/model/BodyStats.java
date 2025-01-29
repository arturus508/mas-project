package com.example.healthfitness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class BodyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bodyStatsId;

    private String dateRecorded;

    private double weight;

    private double bodyFatPercent;

    private double BMI;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;



    // Getters and setters
    public Long getBodyStatsId() {
        return bodyStatsId;
    }

    // Default constructor
    public BodyStats() {
    }

    // Constructor with arguments
    public BodyStats(String dateRecorded, double weight, double bodyFatPercent) {
        this.dateRecorded = dateRecorded;
        this.weight = weight;
        this.bodyFatPercent = bodyFatPercent;
    }


    public void setBodyStatsId(Long bodyStatsId) {
        this.bodyStatsId = bodyStatsId;
    }

    public String getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(String dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBodyFatPercent() {
        return bodyFatPercent;
    }

    public void setBodyFatPercent(double bodyFatPercent) {
        this.bodyFatPercent = bodyFatPercent;
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

