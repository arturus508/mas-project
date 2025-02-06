package com.example.healthfitness.controller;

import com.example.healthfitness.model.User;
import com.example.healthfitness.service.NotificationService;
import com.example.healthfitness.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
public class NotificationViewController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationViewController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public String getNotificationsPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        model.addAttribute("notifications", notificationService.getNotificationsByUserId(user.getUserId()));
        model.addAttribute("user", user);
        return "notifications"; // corresponds to notifications.html
    }

    // New endpoint: deletes the notification and then redirects back.
    @GetMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }
}





