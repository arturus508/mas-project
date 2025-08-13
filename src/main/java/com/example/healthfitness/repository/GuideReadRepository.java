package com.example.healthfitness.repository;

import com.example.healthfitness.model.Guide;
import com.example.healthfitness.model.GuideRead;
import com.example.healthfitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuideReadRepository extends JpaRepository<GuideRead, Long> {
    Optional<GuideRead> findByUserAndGuide(User user, Guide guide);
    List<GuideRead> findTop5ByUserOrderByLastViewedAtDesc(User user);
}
