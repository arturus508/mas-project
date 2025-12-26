package com.example.healthfitness.controller;

import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.service.SleepService;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.web.form.SleepForm;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

/**
 * Controller for the sleep tracking page.  The old implementation relied
 * on {@code SecurityContextHolder} to resolve the current user via
 * email.  This refactored version uses {@link CurrentUserService} to
 * obtain the current user's id. All dates use {@link LocalDate} or
 * {@link LocalDateTime} rather than strings, and query parameters are
 * converted using {@link DateTimeFormat} annotations.
 */
@Controller
@RequestMapping("/mind/sleep")
public class SleepViewController {

    private final SleepService sleepService;
    private final CurrentUserService currentUserService;

    public SleepViewController(SleepService sleepService, CurrentUserService currentUserService) {
        this.sleepService = sleepService;
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
        SleepForm form = new SleepForm();
        form.setDate(LocalDate.now());
        form.setHours(7);
        form.setMinutes(30);
        form.setQuality(3);
        model.addAttribute("sleepForm", form);
        return "sleep";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("sleepForm") SleepForm form,
                      BindingResult bindingResult,
                      @RequestParam(name = "ym", required = false)
                      @DateTimeFormat(pattern = "yyyy-MM") YearMonth ym,
                      Model model) {
        Long userId = currentUserService.id();
        if (bindingResult.hasErrors()) {
            var month = ym != null ? ym : YearMonth.now();
            List<SleepEntry> entries = sleepService.listForMonth(userId, month);
            model.addAttribute("entries", entries);
            model.addAttribute("month", month);
            return "sleep";
        }
        sleepService.create(userId, form);
        return "redirect:/mind/sleep";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Long userId = currentUserService.id();
        sleepService.delete(userId, id);
        return "redirect:/mind/sleep";
    }
}
