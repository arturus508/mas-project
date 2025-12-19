package com.example.healthfitness.controller;

import com.example.healthfitness.service.TaskService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.CurrentUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller for managing daily flow tasks.  Previously the code
 * retrieved the current user's email from {@code SecurityContextHolder};
 * this refactoring uses {@link CurrentUserService} to obtain the user id
 * directly.  All dates passed via query parameters are bound to
 * {@link LocalDate} using the {@link DateTimeFormat} annotation.
 */
@Controller
@RequestMapping("/flow/tasks")
public class FlowTaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public FlowTaskController(TaskService taskService,
                              UserService userService,
                              CurrentUserService currentUserService) {
        this.taskService = taskService;
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String list(@RequestParam(value = "date", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       Model model) {
        var d = (date != null) ? date : LocalDate.now();
        Long userId = currentUserService.id();
        model.addAttribute("date", d);
        model.addAttribute("tasks", taskService.listForDate(userId, d));
        return "tasks";
    }

    @PostMapping("/add")
    public String add(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      @RequestParam String title) {
        Long userId = currentUserService.id();
        taskService.add(userId, date, title);
        return "redirect:/flow/tasks?date=" + date;
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id,
                         @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        taskService.toggle(id);
        return "redirect:/flow/tasks?date=" + date;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        taskService.delete(id);
        return "redirect:/flow/tasks?date=" + date;
    }
}