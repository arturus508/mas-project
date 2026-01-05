package com.example.healthfitness.service;

import com.example.healthfitness.exception.ConflictException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private BodyStatsRepository bodyStatsRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        // Persist the user without handling membership or payments (module removed).
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public void updateName(Long userId, String name) {
        User user = getUserById(userId);
        user.setName(name);
        userRepository.save(user);
    }

    public boolean checkPassword(Long userId, String rawPassword) {
        User user = getUserById(userId);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public void updatePassword(Long userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<WorkoutPlan> getWorkoutPlansByUserId(Long userId) {
        getUserById(userId);
        // Fetch workout plans directly via repository to avoid lazy-loading issues. This retrieves
        // all workout plans associated with the given user id without requiring the user entity
        // to remain in an active Hibernate session.
        return workoutPlanRepository.findByUser_UserId(userId);
    }

    public List<MealPlan> getMealPlansByUserId(Long userId) {
        getUserById(userId);
        // Fetch meal plans directly via repository to avoid LazyInitializationException.
        return mealPlanRepository.findByUser_UserId(userId);
    }

    public List<BodyStats> getBodyStatsByUserId(Long userId) {
        getUserById(userId);
        // Retrieve body stats in a safe way via repository ordering by date recorded descending.
        return bodyStatsRepository.findByUserUserIdOrderByDateRecordedDesc(userId);
    }


    public WorkoutPlan addWorkoutPlanToUser(Long userId, WorkoutPlan workoutPlan) {
        User user = getUserById(userId);
        workoutPlan.setUser(user);
        return workoutPlanRepository.save(workoutPlan);
    }

    public MealPlan addMealPlanToUser(Long userId, MealPlan mealPlan) {
        User user = getUserById(userId);
        mealPlan.setUser(user);
        return mealPlanRepository.save(mealPlan);
    }

    public BodyStats addBodyStatsToUser(Long userId, BodyStats bodyStats) {
        User user = getUserById(userId);
        bodyStats.setUser(user);
        if (bodyStats.getDateRecorded() == null) {
            return bodyStatsRepository.save(bodyStats);
        }
        List<BodyStats> existing =
                bodyStatsRepository.findByUserUserIdAndDateRecorded(userId, bodyStats.getDateRecorded());
        if (existing.isEmpty()) {
            return bodyStatsRepository.save(bodyStats);
        }
        existing.sort(java.util.Comparator.comparing(BodyStats::getBodyStatsId));
        BodyStats latest = existing.get(existing.size() - 1);
        latest.setWeight(bodyStats.getWeight());
        latest.setBodyFatPercent(bodyStats.getBodyFatPercent());
        latest.setDateRecorded(bodyStats.getDateRecorded());
        BodyStats saved = bodyStatsRepository.save(latest);
        for (int i = 0; i < existing.size() - 1; i++) {
            bodyStatsRepository.delete(existing.get(i));
        }
        return saved;
    }

    // Removed methods related to payments and membership because those modules have been removed

    public User registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email.toLowerCase()).isPresent()) {
            throw new ConflictException("User already exists");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(name, email.toLowerCase(), hashedPassword);
        return userRepository.save(user);
    }

    public boolean authenticate(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase());
        return userOpt.isPresent() && passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
    }
}
