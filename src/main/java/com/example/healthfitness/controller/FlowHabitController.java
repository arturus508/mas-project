package com.example.healthfitness.controller;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.HabitLogService;
import com.example.healthfitness.service.HabitService;
import com.example.healthfitness.service.HabitStatsService;
import com.example.healthfitness.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final CurrentUserService currentUserService;

    public FlowHabitController(HabitService habitService ,
                               HabitLogService habitLogService ,
                               HabitStatsService habitStatsService ,
                               UserService userService,
                               CurrentUserService currentUserService){
        this.habitService = habitService; this.habitLogService = habitLogService;
        this.habitStatsService = habitStatsService; this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String list(@RequestParam(required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       Model model){
        Long userId = currentUserService.id();
        User u = userService.getUserById(userId);
        LocalDate ref = (date == null) ? LocalDate.now() : date;

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
                         @RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        Long userId = currentUserService.id();
        User u = userService.getUserById(userId);

        Habit h = new Habit();
        h.setName(name); h.setTargetPerDay(targetPerDay == null ? 1 : targetPerDay);
        h.setActive(true); h.setUser(u);
        habitService.save(h);

        LocalDate d = (date == null) ? LocalDate.now() : date;
        return "redirect:/flow/habits?date=" + d;
    }

    @PostMapping("/toggle/{habitId}")
    public String toggle(@PathVariable Long habitId,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         @RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate refDate){
        Long userId = currentUserService.id();
        User u = userService.getUserById(userId);
        Habit h = habitService.getForUserOrThrow(userId, habitId);
        LocalDate d = date != null ? date : LocalDate.now();
        habitLogService.toggle(u , h , d);
        LocalDate back = (refDate == null) ? d : refDate;
        return "redirect:/flow/habits?date=" + back;
    }

    @PostMapping("/{habitId}/delete")
    public String delete(@PathVariable Long habitId,
                         @RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        Long userId = currentUserService.id();
        Habit h = habitService.getForUserOrThrow(userId, habitId);
        habitLogService.deleteAllForHabit(h); habitService.delete(h);
        LocalDate d = (date == null) ? LocalDate.now() : date;
        return "redirect:/flow/habits?date=" + d;
    }
}


