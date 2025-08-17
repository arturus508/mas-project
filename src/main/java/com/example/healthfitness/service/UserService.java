package com.example.healthfitness.service;

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
    private MembershipRepository membershipRepository;

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private BodyStatsRepository bodyStatsRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public List<WorkoutPlan> getWorkoutPlansByUserId(Long userId) {
        return getUserById(userId).getWorkoutPlans();
    }

    public List<MealPlan> getMealPlansByUserId(Long userId) {
        return getUserById(userId).getMealPlans();
    }

    public List<BodyStats> getBodyStatsByUserId(Long userId) {
        return getUserById(userId).getBodyStats();
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

    public Payment addPaymentToUser(Long userId, Payment payment) {
        User user = getUserById(userId);
        payment.setUser(user);
        return paymentRepository.save(payment);
    }

    public User registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email.toLowerCase()).isPresent()) {
            throw new RuntimeException("User already exists!");
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




