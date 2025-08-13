package com.example.healthfitness.controller;

import com.example.healthfitness.model.Membership;
import com.example.healthfitness.service.MembershipService;
import com.example.healthfitness.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentViewController {

    private final MembershipService membershipService;
    private final UserService userService;

    public PaymentViewController(MembershipService membershipService,
                                 UserService userService) {
        this.membershipService = membershipService;
        this.userService = userService;
    }

    @GetMapping("/payment")
    public String showPaymentPage(Model model) {
        Membership membership = membershipService.getCurrentMembership();
        model.addAttribute("membership", membership);
        return "payment";
    }

    @GetMapping("/payment/confirm")
    public String confirmPayment() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        // In future, you can trigger confirmation logic here
        return "redirect:/membership";
    }

    @GetMapping("/payment/cancel")
    public String cancelPayment() {
        membershipService.cancelMembership(1L);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        return "redirect:/membership";
    }
}

