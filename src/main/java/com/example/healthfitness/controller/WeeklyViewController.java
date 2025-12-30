package com.example.healthfitness.controller;

import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.WeeklyViewService;
import com.example.healthfitness.web.view.WeeklyViewModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/week")
public class WeeklyViewController {

    private final WeeklyViewService weeklyViewService;
    private final CurrentUserService currentUserService;

    public WeeklyViewController(WeeklyViewService weeklyViewService,
                                CurrentUserService currentUserService) {
        this.weeklyViewService = weeklyViewService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String thisWeek(Model model) {
        return week(LocalDate.now(), model);
    }

    @GetMapping("/{date}")
    public String week(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       Model model) {
        Long userId = currentUserService.id();
        WeeklyViewModel vm = weeklyViewService.build(userId, date);
        model.addAttribute("vm", vm);
        return "weekly";
    }
}
