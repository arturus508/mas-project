package com.example.healthfitness.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileNameForm {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
