package com.example.healthfitness.controller;

import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.service.SleepService;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@Controller
@RequestMapping("/mind/sleep")
public class SleepViewController {

    @Autowired
    private SleepService sleepService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String view(@RequestParam(name = "ym", required = false)
                       @DateTimeFormat(pattern = "yyyy-MM") YearMonth ym,
                       Model model) {
        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var user  = userService.findByEmail(email).orElseThrow();

        var month = ym != null ? ym : YearMonth.now();
        List<SleepEntry> entries = sleepService.listForMonth(user.getUserId(), month);

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

        var auth  = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        var user  = userService.findByEmail(email).orElseThrow();

        var e = new SleepEntry();
        e.setSleepStart(sleepStart);
        e.setSleepEnd(sleepEnd);
        e.setQuality(quality);
        e.setNote(note);
        if (date != null) e.setDate(date);

        sleepService.save(user.getUserId(), e);
        return "redirect:/mind/sleep";
    }
}
