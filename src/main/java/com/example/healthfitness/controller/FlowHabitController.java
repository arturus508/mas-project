package com.example.healthfitness.controller;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.HabitLogService;
import com.example.healthfitness.service.HabitService;
import com.example.healthfitness.service.HabitStatsService;
import com.example.healthfitness.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/flow/habits")
public class FlowHabitController {

    private static final int HEATMAP_DAYS = 28;

    private final HabitService habitService;
    private final HabitLogService habitLogService;
    private final HabitStatsService habitStatsService;
    private final UserService userService;

    public FlowHabitController(HabitService habitService ,
                               HabitLogService habitLogService ,
                               HabitStatsService habitStatsService ,
                               UserService userService){
        this.habitService = habitService; this.habitLogService = habitLogService;
        this.habitStatsService = habitStatsService; this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String date , Principal principal , Model model){
        User u = userService.findByEmail(principal.getName()).orElseThrow();
        LocalDate ref = (date == null || date.isBlank()) ? LocalDate.now() : LocalDate.parse(date);

        List<Habit> habits = habitService.findByUser(u);
        Map<Long,Boolean> today = new HashMap<>( habitLogService.mapForDate(u , ref) );
        for (Habit h : habits) today.putIfAbsent(h.getId() , false);

        var days    = habitStatsService.windowDays(ref , HEATMAP_DAYS);
        var byDate  = habitStatsService.mapsForDates(u , days);
        var heat    = habitStatsService.heatForDays(habits , days , byDate);
        var streaks = habitStatsService.streaks(u , habits , ref , HEATMAP_DAYS);

        model.addAttribute("habits" , habits);
        model.addAttribute("today" , today);
        model.addAttribute("date" , ref.toString());
        model.addAttribute("windowDays" , days);
        model.addAttribute("heat" , heat);
        model.addAttribute("streaks" , streaks);
        return "habits";
    }

    @PostMapping
    public String create(@RequestParam String name ,
                         @RequestParam(required = false , defaultValue = "1") Integer targetPerDay ,
                         @RequestParam(required = false) String date ,
                         Principal principal){
        User u = userService.findByEmail(principal.getName()).orElseThrow();

        Habit h = new Habit();
        h.setName(name); h.setTargetPerDay(targetPerDay == null ? 1 : targetPerDay);
        h.setActive(true); h.setUser(u);
        habitService.save(h);

        String d = (date == null || date.isBlank()) ? LocalDate.now().toString() : date;
        return "redirect:/flow/habits?date=" + d;
    }

    @PostMapping("/toggle/{habitId}")
    public String toggle(@PathVariable Long habitId , @RequestParam String date ,
                         @RequestParam(required = false) String refDate , Principal principal){
        User u = userService.findByEmail(principal.getName()).orElseThrow();
        Habit h = habitService.getOrThrow(habitId);
        LocalDate d = LocalDate.parse(date);
        habitLogService.toggle(u , h , d);
        String back = (refDate == null || refDate.isBlank()) ? d.toString() : refDate;
        return "redirect:/flow/habits?date=" + back;
    }

    @PostMapping("/{habitId}/delete")
    public String delete(@PathVariable Long habitId , @RequestParam(required = false) String date){
        Habit h = habitService.getOrThrow(habitId);
        habitLogService.deleteAllForHabit(h); habitService.delete(h);
        String d = (date == null || date.isBlank()) ? LocalDate.now().toString() : date;
        return "redirect:/flow/habits?date=" + d;
    }
}


