package com.example.healthfitness.service;

import com.example.healthfitness.model.Membership;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.MembershipRepository;
import com.example.healthfitness.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    public Membership getCurrentMembership() {
        // For demonstration, we use membership ID 1.
        return membershipRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
    }

    // Cancel membership: Reset to "Standard Inactive" with price 0.
    public void cancelMembership(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        membership.setStatus("Inactive");
        membership.setMembershipType("Standard");
        membership.setPrice(0.0);
        membershipRepository.save(membership);
    }

    // Upgrade membership:
    // - If currently Inactive, set to Active Standard with base price ($10).
    // - If currently Active Standard, set to Premium Active (increase price by $20).
    public void upgradeMembership(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        if ("Inactive".equalsIgnoreCase(membership.getStatus())) {
            membership.setStatus("Active");
            membership.setMembershipType("Standard");
            membership.setPrice(10.0);
        } else if ("Active".equalsIgnoreCase(membership.getStatus()) &&
                "Standard".equalsIgnoreCase(membership.getMembershipType())) {
            membership.setMembershipType("Premium");
            membership.setPrice(membership.getPrice() + 20.0);
        }
        membershipRepository.save(membership);
    }

    public Membership saveMembershipForUser(Long userId, Membership membership) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        membership.setUser(user);
        return membershipRepository.save(membership);
    }
}



