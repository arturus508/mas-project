package com.example.healthfitness.controller;

import com.example.healthfitness.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/notifications")
public class NotificationViewController {

    private final NotificationService notificationService;

    public NotificationViewController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String getNotificationsPage(@RequestParam Long userId, Model model) {
        model.addAttribute("notifications", notificationService.getNotificationsByUserId(userId));
        return "notifications";
    }
}

