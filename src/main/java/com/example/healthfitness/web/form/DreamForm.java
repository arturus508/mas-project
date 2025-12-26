package com.example.healthfitness.web.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class DreamForm {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotBlank
    @Size(max = 120)
    private String title;

    @NotBlank
    @Size(max = 4000)
    private String content;

    @Size(max = 255)
    private String tags;

    @Min(1)
    @Max(5)
    private Integer mood;

    private Boolean lucid;
    private Boolean nightmare;

    private Long sleepEntryId;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Integer getMood() { return mood; }
    public void setMood(Integer mood) { this.mood = mood; }

    public Boolean getLucid() { return lucid; }
    public void setLucid(Boolean lucid) { this.lucid = lucid; }

    public Boolean getNightmare() { return nightmare; }
    public void setNightmare(Boolean nightmare) { this.nightmare = nightmare; }

    public Long getSleepEntryId() { return sleepEntryId; }
    public void setSleepEntryId(Long sleepEntryId) { this.sleepEntryId = sleepEntryId; }
}
