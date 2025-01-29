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

        return membershipRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
    }

    public void cancelMembership(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        membership.setStatus("Cancelled");
        membershipRepository.save(membership);
    }
    public Membership saveMembershipForUser(Long userId, Membership membership) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        membership.setUser(user);
        return membershipRepository.save(membership);
    }
    public void upgradeMembership(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        membership.setMembershipType("Premium");
        membership.setPrice(membership.getPrice() + 20.0);
        membershipRepository.save(membership);
    }
}



