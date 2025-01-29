package com.example.healthfitness.controller;

import com.example.healthfitness.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping("/membership")
    public String showMembership(Model model) {
        model.addAttribute("membership", membershipService.getCurrentMembership());
        return "membership"; // Returns membership.html
    }

    @GetMapping("/membership/upgrade")
    public String upgradeMembership() {
        membershipService.upgradeMembership(1L); // Replace 1L with actual membership ID
        return "redirect:/membership";
    }

    @GetMapping("/membership/cancel")
    public String cancelMembership() {
        membershipService.cancelMembership(1L); // Replace 1L with actual membership ID
        return "redirect:/membership";
    }
}

