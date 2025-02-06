package com.example.healthfitness.service;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private BodyStatsRepository bodyStatsRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // We'll use this to hash once

    // Save user (not specifically for registration)
    public User saveUser(User user) {
        // If there's membership, we find and set it
        if (user.getMembership() != null) {
            Membership membership = membershipRepository.findById(user.getMembership().getMembershipId())
                    .orElseThrow(() -> new RuntimeException("Membership not found"));
            user.setMembership(membership);
        }
        return userRepository.save(user);
    }

    // Return all users (only "User" type in SINGLE_TABLE)
    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    // Return user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Return user by email (optional)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    // Example usage to get workout plans, meal plans, etc.
    public List<WorkoutPlan> getWorkoutPlansByUserId(Long userId) {
        return getUserById(userId).getWorkoutPlans();
    }

    public List<MealPlan> getMealPlansByUserId(Long userId) {
        return getUserById(userId).getMealPlans();
    }

    public List<BodyStats> getBodyStatsByUserId(Long userId) {
        return getUserById(userId).getBodyStats();
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return getUserById(userId).getNotifications();
    }

    public List<Payment> getPaymentsByUserId(Long userId) {
        return getUserById(userId).getPayments();
    }

    public Membership getMembershipByUserId(Long userId) {
        return getUserById(userId).getMembership();
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
        return bodyStatsRepository.save(bodyStats);
    }

    public Notification addNotificationToUser(Long userId, Notification notification) {
        User user = getUserById(userId);
        notification.setUser(user);
        return notificationRepository.save(notification);
    }

    public Payment addPaymentToUser(Long userId, Payment payment) {
        User user = getUserById(userId);
        payment.setUser(user);
        return paymentRepository.save(payment);
    }

    // The main registration method that hashes the password exactly once
    public User registerUser(String name, String email, String password) {
        // Check if user with the same email exists
        if (userRepository.findByEmail(email.toLowerCase()).isPresent()) {
            throw new RuntimeException("User already exists!");
        }

        // Hash the password once
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(name, email.toLowerCase(), hashedPassword);

        // Save new user
        return userRepository.save(user);
    }

    // For manual authentication if needed
    public boolean authenticate(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase());
        return userOpt.isPresent() && passwordEncoder.matches(rawPassword, userOpt.get().getPassword());
    }
}




