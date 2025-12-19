package com.example.healthfitness.controller;

import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.service.SleepService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.CurrentUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

/**
 * Controller for the sleep tracking page.  The old implementation relied
 * on {@code SecurityContextHolder} to resolve the current user via
 * email.  This refactored version uses {@link CurrentUserService} to
 * obtain the current user's id and then looks up the user entity via
 * {@link UserService}.  All dates use {@link LocalDate} or
 * {@link LocalDateTime} rather than strings, and query parameters are
 * converted using {@link DateTimeFormat} annotations.
 */
@Controller
@RequestMapping("/mind/sleep")
public class SleepViewController {

    private final SleepService sleepService;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public SleepViewController(SleepService sleepService, UserService userService, CurrentUserService currentUserService) {
        this.sleepService = sleepService;
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String view(@RequestParam(name = "ym", required = false)
                       @DateTimeFormat(pattern = "yyyy-MM") YearMonth ym,
                       Model model) {
        Long userId = currentUserService.id();
        var month = ym != null ? ym : YearMonth.now();
        List<SleepEntry> entries = sleepService.listForMonth(userId, month);
        model.addAttribute("entries", entries);
        model.addAttribute("month", month);
        return "sleep";
    }

    @PostMapping("/add")
    public String add(@RequestParam("sleepStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sleepStart,
                      @RequestParam("sleepEnd")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sleepEnd,
                      @RequestParam("quality")    Integer quality,
                      @RequestParam(value = "note", required = false) String note,
                      @RequestParam(value = "date", required = false)
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = currentUserService.id();
        var e = new SleepEntry();
        e.setSleepStart(sleepStart);
        e.setSleepEnd(sleepEnd);
        e.setQuality(quality);
        e.setNote(note);
        if (date != null) {
            e.setDate(date);
        }
        sleepService.save(userId, e);
        return "redirect:/mind/sleep";
    }
}