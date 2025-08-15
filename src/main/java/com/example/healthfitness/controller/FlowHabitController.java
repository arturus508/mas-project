package com.example.healthfitness.controller;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.HabitCadence;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.HabitLogService;
import com.example.healthfitness.service.HabitService;
import com.example.healthfitness.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/habits")
public class FlowHabitController {

    private final HabitService habitService;
    private final HabitLogService habitLogService;
    private final UserService userService;

    public FlowHabitController(HabitService habitService ,
                               HabitLogService habitLogService ,
                               UserService userService){
        this.habitService    = habitService;
        this.habitLogService = habitLogService;
        this.userService     = userService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String date ,
                       Principal principal ,
                       Model model){
        User u = userService.findByEmail(principal.getName()).orElseThrow();
        LocalDate d = (date == null || date.isBlank()) ? LocalDate.now() : LocalDate.parse(date);

        List<Habit> habits = habitService.findByUser(u);
        Map<Long,Boolean> today = habitLogService.mapForDate(u , d);

        model.addAttribute("habits" , habits);
        model.addAttribute("today"  , today);
        model.addAttribute("date"   , d.toString());
        return "habits";
    }

    @PostMapping
    public String create(@RequestParam String name ,
                         @RequestParam String cadence ,
                         @RequestParam(required = false , defaultValue = "1") Integer targetPerDay ,
                         @RequestParam(required = false) String date ,
                         Principal principal){
        User u = userService.findByEmail(principal.getName()).orElseThrow();

        Habit h = new Habit();
        h.setName(name);
        h.setCadence(HabitCadence.valueOf(cadence));
        h.setTargetPerDay(targetPerDay == null ? 1 : targetPerDay);
        h.setUser(u);
        habitService.save(h);

        String d = (date == null || date.isBlank()) ? LocalDate.now().toString() : date;
        return "redirect:/flow/habits?date=" + d;
    }

    @PostMapping("/toggle/{habitId}")
    public String toggle(@PathVariable Long habitId ,
                         @RequestParam String date ,
                         Principal principal){
        User u = userService.findByEmail(principal.getName()).orElseThrow();
        Habit h = habitService.getOrThrow(habitId);

        LocalDate d = LocalDate.parse(date);
        habitLogService.toggle(h , u , d);
        return "redirect:/flow/habits?date=" + d;
    }

    @PostMapping("/{habitId}/delete")
    public String delete(@PathVariable Long habitId ,
                         @RequestParam(required = false) String date){
        Habit h = habitService.getOrThrow(habitId);

        habitLogService.deleteAllForHabit(h);
        habitService.delete(h);

        String d = (date == null || date.isBlank()) ? LocalDate.now().toString() : date;
        return "redirect:/flow/habits?date=" + d;
    }
}
