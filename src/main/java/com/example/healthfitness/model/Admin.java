package com.example.healthfitness.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {
    private int adminLevel;

    public Admin() {}

    public Admin(String name, String email, String password, int adminLevel) {
        super(name, email, password);
        this.adminLevel = adminLevel;
    }

    public int getAdminLevel() { return adminLevel; }
    public void setAdminLevel(int adminLevel) { this.adminLevel = adminLevel; }
}


