package com.example.healthfitness.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/week")
public class WeeklyViewController {

    @GetMapping
    public String thisWeek(@RequestParam(value = "mode", required = false) String mode,
                           @RequestParam(value = "date", required = false) String dateParam) {
        String cleanMode = sanitizeMode(mode);
        LocalDate date = parseDate(dateParam, LocalDate.now());
        return "redirect:/today?mode=" + cleanMode + "&range=week&date=" + date;
    }

    @GetMapping("/{date}")
    public String week(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       @RequestParam(value = "mode", required = false) String mode) {
        String cleanMode = sanitizeMode(mode);
        return "redirect:/today?mode=" + cleanMode + "&range=week&date=" + date;
    }

    private String sanitizeMode(String value) {
        if (value == null) {
            return "body";
        }
        String normalized = value.trim().toLowerCase();
        return ("mind".equals(normalized) || "body".equals(normalized)) ? normalized : "body";
    }

    private LocalDate parseDate(String value, LocalDate fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException ex) {
            return fallback;
        }
    }
}
