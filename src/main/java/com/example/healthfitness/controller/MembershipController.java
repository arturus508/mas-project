package com.example.healthfitness.controller;

import com.example.healthfitness.model.Membership;
import com.example.healthfitness.model.Notification;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.MembershipService;
import com.example.healthfitness.service.NotificationService;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping("/membership")
    public String showMembership(Model model) {
        Membership membership = membershipService.getCurrentMembership();
        model.addAttribute("membership", membership);
        return "membership"; // Renders membership.html
    }

    @GetMapping("/membership/upgrade")
    public String upgradeMembership() {
        // For demo, we use membership ID 1.
        membershipService.upgradeMembership(1L);
        // Get current user from security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Notification notif = new Notification();
        notif.setMessage("Membership upgraded successfully.");
        notif.setNotificationType("Membership");
        notif.setDateCreated(LocalDate.now().toString());
        notif.setRead(false);
        notif.setUser(currentUser);
        notificationService.saveNotification(notif);
        return "redirect:/membership";
    }

    @GetMapping("/membership/cancel")
    public String cancelMembership() {
        membershipService.cancelMembership(1L);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Notification notif = new Notification();
        notif.setMessage("Payment cancelled: Membership cancelled.");
        notif.setNotificationType("Membership");
        notif.setDateCreated(LocalDate.now().toString());
        notif.setRead(false);
        notif.setUser(currentUser);
        notificationService.saveNotification(notif);
        return "redirect:/membership";
    }

    @GetMapping("/membership/payment")
    public String membershipPayment() {
        // Instead of processing payment immediately, this endpoint now shows the Payment page.
        return "redirect:/payment";
    }
}





