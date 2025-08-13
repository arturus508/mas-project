package com.example.healthfitness.controller;

import com.example.healthfitness.model.Membership;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.MembershipService;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private UserService userService;

    @GetMapping("/membership")
    public String showMembership(Model model) {
        Membership membership = membershipService.getCurrentMembership();
        model.addAttribute("membership", membership);
        return "membership";
    }

    @GetMapping("/membership/upgrade")
    public String upgradeMembership() {
        membershipService.upgradeMembership(1L);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        return "redirect:/membership";
    }

    @GetMapping("/membership/cancel")
    public String cancelMembership() {
        membershipService.cancelMembership(1L);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        return "redirect:/membership";
    }

    @GetMapping("/membership/payment")
    public String membershipPayment() {
        return "redirect:/payment";
    }
}



