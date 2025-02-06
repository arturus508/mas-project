package com.example.healthfitness.controller;

import com.example.healthfitness.model.Membership;
import com.example.healthfitness.model.Notification;
import com.example.healthfitness.service.MembershipService;
import com.example.healthfitness.service.NotificationService;
import com.example.healthfitness.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class PaymentViewController {

    private final MembershipService membershipService;
    private final NotificationService notificationService;
    private final UserService userService;

    public PaymentViewController(MembershipService membershipService,
                                 NotificationService notificationService,
                                 UserService userService) {
        this.membershipService = membershipService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    // Display the Payment page.
    @GetMapping("/payment")
    public String showPaymentPage(Model model) {
        Membership membership = membershipService.getCurrentMembership();
        model.addAttribute("membership", membership);
        return "payment"; // renders payment.html
    }

    // Confirm payment: simulate a successful payment.
    @GetMapping("/payment/confirm")
    public String confirmPayment() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        // Use current user directly
        var currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Notification notif = new Notification();
        notif.setMessage("Membership paid successfully.");
        notif.setNotificationType("Membership");
        notif.setDateCreated(LocalDate.now().toString());
        notif.setRead(false);
        notif.setUser(currentUser);
        notificationService.saveNotification(notif);
        return "redirect:/membership";
    }

    // Cancel payment from the Payment page: simulate cancellation.
    @GetMapping("/payment/cancel")
    public String cancelPayment() {
        membershipService.cancelMembership(1L);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        var currentUser = userService.findByEmail(email)
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
}


