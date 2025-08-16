package com.example.healthfitness.controller;

import com.example.healthfitness.service.TaskService;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/flow/tasks")
public class FlowTaskController {

    @Autowired private TaskService taskService;
    @Autowired private UserService userService;

    @GetMapping
    public String list(@RequestParam(value = "date" , required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date ,
                       Model model){
        var d = (date != null) ? date : LocalDate.now();
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user  = userService.findByEmail(email).orElseThrow();

        model.addAttribute("date" , d);
        model.addAttribute("tasks", taskService.listForDate(user.getUserId() , d));
        return "tasks";
    }

    @PostMapping("/add")
    public String add(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date ,
                      @RequestParam String title){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user  = userService.findByEmail(email).orElseThrow();
        taskService.add(user.getUserId() , date , title);
        return "redirect:/flow/tasks?date=" + date;
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id ,
                         @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        taskService.toggle(id);
        return "redirect:/flow/tasks?date=" + date;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id ,
                         @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        taskService.delete(id);
        return "redirect:/flow/tasks?date=" + date;
    }
}
