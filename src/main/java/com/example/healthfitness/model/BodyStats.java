package com.example.healthfitness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Encja przechowująca pojedynczy wpis ze statystykami ciała użytkownika.
 * Korzysta z {@link LocalDate} dla daty, aby uniknąć problemów z parsowaniem
 * i sortowaniem po Stringu.
 */
@Entity
public class BodyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bodyStatsId;

  
    private LocalDate dateRecorded;

    private double weight;
    private double bodyFatPercent;
    private double BMI;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    // Konstruktor domyślny wymagany przez JPA
    public BodyStats() {}

    /**
     * Tworzy obiekt statystyk ciała na konkretny dzień.
     * @param dateRecorded data wpisu
     * @param weight waga użytkownika
     * @param bodyFatPercent procent tkanki tłuszczowej
     */
    public BodyStats(LocalDate dateRecorded, double weight, double bodyFatPercent) {
        this.dateRecorded   = dateRecorded;
        this.weight         = weight;
        this.bodyFatPercent = bodyFatPercent;
    }

    public Long getBodyStatsId() { return bodyStatsId; }
    public void setBodyStatsId(Long id) { this.bodyStatsId = id; }

    public LocalDate getDateRecorded() { return dateRecorded; }
    public void setDateRecorded(LocalDate dateRecorded) { this.dateRecorded = dateRecorded; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getBodyFatPercent() { return bodyFatPercent; }
    public void setBodyFatPercent(double bodyFatPercent) { this.bodyFatPercent = bodyFatPercent; }

    public double getBMI() { return BMI; }
    public void setBMI(double BMI) { this.BMI = BMI; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
