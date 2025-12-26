package com.example.healthfitness.controller;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.HabitLogService;
import com.example.healthfitness.service.HabitService;
import com.example.healthfitness.service.TaskService;
import com.example.healthfitness.service.TodayViewService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.web.view.TodayViewModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/today")
public class TodayViewController {

    private final TodayViewService todayViewService;
    private final CurrentUserService currentUserService;
    private final TaskService taskService;
    private final HabitService habitService;
    private final HabitLogService habitLogService;
    private final UserService userService;

    public TodayViewController(TodayViewService todayViewService,
                               CurrentUserService currentUserService,
                               TaskService taskService,
                               HabitService habitService,
                               HabitLogService habitLogService,
                               UserService userService) {
        this.todayViewService = todayViewService;
        this.currentUserService = currentUserService;
        this.taskService = taskService;
        this.habitService = habitService;
        this.habitLogService = habitLogService;
        this.userService = userService;
    }

    @GetMapping
    public String today(Model model) {
        return day(LocalDate.now(), model);
    }

    @GetMapping("/{date}")
    public String day(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      Model model) {
        Long userId = currentUserService.id();
        TodayViewModel vm = todayViewService.build(userId, date);
        model.addAttribute("vm", vm);
        return "today";
    }

    @PostMapping("/{date}/tasks/{id}/toggle")
    public String toggleTask(@PathVariable Long id,
                             @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = currentUserService.id();
        taskService.toggle(userId, id);
        return "redirect:/today/" + date;
    }

    @PostMapping("/{date}/habits/{id}/toggle")
    public String toggleHabit(@PathVariable Long id,
                              @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = currentUserService.id();
        User user = userService.getUserById(userId);
        Habit habit = habitService.getForUserOrThrow(userId, id);
        habitLogService.toggle(user, habit, date);
        return "redirect:/today/" + date;
    }
}
