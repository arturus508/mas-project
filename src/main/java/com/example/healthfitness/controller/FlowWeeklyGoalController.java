package com.example.healthfitness.controller;

import com.example.healthfitness.model.WeeklyGoalType;
import com.example.healthfitness.service.WeeklyGoalService;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/flow/weekly-goals")
public class FlowWeeklyGoalController {

    @Autowired private WeeklyGoalService weeklyGoalService;
    @Autowired private UserService userService;

    @GetMapping
    public String list(Model model){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user  = userService.findByEmail(email).orElseThrow();

        weeklyGoalService.finalizeFinishedWeeks(user.getUserId());

        model.addAttribute("goals", weeklyGoalService.listForUser(user.getUserId()));
        model.addAttribute("types", WeeklyGoalType.values());
        model.addAttribute("defStart", WeeklyGoalService.firstDayOfThisWeek());
        model.addAttribute("defEnd",   WeeklyGoalService.lastDayOfThisWeek());
        return "weekly-goals";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title ,
                      @RequestParam WeeklyGoalType type ,
                      @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart ,
                      @RequestParam("weekEnd")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekEnd ,
                      @RequestParam Integer targetValue){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user  = userService.findByEmail(email).orElseThrow();
        weeklyGoalService.add(user.getUserId(), title, type, weekStart, weekEnd, targetValue);
        return "redirect:/flow/weekly-goals";
    }

    @PostMapping("/inc/{id}")
    public String inc(@PathVariable Long id , @RequestParam(defaultValue = "1") Integer by){
        weeklyGoalService.increment(id, by!=null? by : 1);
        return "redirect:/flow/weekly-goals";
    }

    @PostMapping("/dec/{id}")
    public String dec(@PathVariable Long id , @RequestParam(defaultValue = "1") Integer by){
        weeklyGoalService.increment(id, -Math.abs(by!=null? by : 1));
        return "redirect:/flow/weekly-goals";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id){
        weeklyGoalService.toggleComplete(id);
        return "redirect:/flow/weekly-goals";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        weeklyGoalService.delete(id);
        return "redirect:/flow/weekly-goals";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id ,
                         @RequestParam String title ,
                         @RequestParam WeeklyGoalType type ,
                         @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart ,
                         @RequestParam("weekEnd")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekEnd ,
                         @RequestParam Integer targetValue ,
                         @RequestParam Integer currentValue ,
                         @RequestParam(defaultValue = "false") boolean completed){
        weeklyGoalService.update(id, title, type, weekStart, weekEnd, targetValue, currentValue, completed);
        return "redirect:/flow/weekly-goals";
    }
}

