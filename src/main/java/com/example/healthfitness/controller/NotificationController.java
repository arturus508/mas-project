package com.example.healthfitness.controller;

import com.example.healthfitness.model.Notification;
import com.example.healthfitness.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getAllNotifications(@RequestParam Long userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markNotificationAsRead(id);
    }
}

