package com.example.healthfitness.repository;

import com.example.healthfitness.model.Guide;
import com.example.healthfitness.model.GuideCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long> {
    Optional<Guide> findBySlugAndIsPublishedTrue(String slug);
    List<Guide> findAllByCategoryAndIsPublishedTrueOrderByCreatedAtDesc(GuideCategory category);
    List<Guide> findTop5ByCategoryAndIsPublishedTrueOrderByEstReadingMinAsc(GuideCategory category);
}
