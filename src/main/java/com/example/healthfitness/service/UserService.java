package com.example.healthfitness.service;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User saveUser(User user) {
        if (user.getMembership() != null) {
            Membership membership = membershipRepository.findById(user.getMembership().getMembershipId())
                    .orElseThrow(() -> new RuntimeException("Membership not found"));
            user.setMembership(membership);
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


    public List<WorkoutPlan> getWorkoutPlansByUserId(Long userId) {
        User user = getUserById(userId);
        return user.getWorkoutPlans();
    }


    public List<MealPlan> getMealPlansByUserId(Long userId) {
        User user = getUserById(userId);
        return user.getMealPlans();
    }


    public List<BodyStats> getBodyStatsByUserId(Long userId) {
        User user = getUserById(userId);
        return user.getBodyStats();
    }


    public List<Notification> getNotificationsByUserId(Long userId) {
        User user = getUserById(userId);
        return user.getNotifications();
    }


    public List<Payment> getPaymentsByUserId(Long userId) {
        User user = getUserById(userId);
        return user.getPayments();
    }


    public Membership getMembershipByUserId(Long userId) {
        User user = getUserById(userId);
        return user.getMembership();
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        notification.setUser(user);
        return notificationRepository.save(notification);
    }
    public Payment addPaymentToUser(Long userId, Payment payment) {
        User user = getUserById(userId);
        payment.setUser(user);
        return paymentRepository.save(payment);
    }

    public User authenticate(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).orElse(null);
    }


}


