package com.example.healthfitness.repository;

import com.example.healthfitness.model.UserSectionVisibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSectionVisibilityRepository extends JpaRepository<UserSectionVisibility, Long> {
    Optional<UserSectionVisibility> findByUser_UserIdAndSectionKey(Long userId, String sectionKey);
    List<UserSectionVisibility> findByUser_UserId(Long userId);
}
