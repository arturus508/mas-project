package com.example.healthfitness.repository;

import com.example.healthfitness.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MembershipRepository extends JpaRepository<Membership, Long> {
}

