package com.example.healthfitness.controller;

import org.springframework.ui.Model;
import com.example.healthfitness.model.*;
import com.example.healthfitness.service.MembershipService;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/profile")
    public String showProfilePage(@RequestParam Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{userId}/workout-plans")
    public List<WorkoutPlan> getWorkoutPlans(@PathVariable Long userId) {
        return userService.getWorkoutPlansByUserId(userId);
    }

    @PostMapping("/{userId}/workout-plans")
    public WorkoutPlan addWorkoutPlan(@PathVariable Long userId, @RequestBody WorkoutPlan workoutPlan) {
        return userService.addWorkoutPlanToUser(userId, workoutPlan);
    }

    @GetMapping("/{userId}/meal-plans")
    public List<MealPlan> getMealPlans(@PathVariable Long userId) {
        return userService.getMealPlansByUserId(userId);
    }

    @PostMapping("/{userId}/meal-plans")
    public MealPlan addMealPlan(@PathVariable Long userId, @RequestBody MealPlan mealPlan) {
        return userService.addMealPlanToUser(userId, mealPlan);
    }

    @GetMapping("/{userId}/body-stats")
    public List<BodyStats> getBodyStats(@PathVariable Long userId) {
        return userService.getBodyStatsByUserId(userId);
    }

    @PostMapping("/{userId}/body-stats")
    public BodyStats addBodyStats(@PathVariable Long userId, @RequestBody BodyStats bodyStats) {
        return userService.addBodyStatsToUser(userId, bodyStats);
    }

    @GetMapping("/{userId}/payments")
    public List<Payment> getPayments(@PathVariable Long userId) {
        return userService.getPaymentsByUserId(userId);
    }

    @PostMapping("/{userId}/payments")
    public Payment addPaymentToUser(@PathVariable Long userId, @RequestBody Payment payment) {
        return userService.addPaymentToUser(userId, payment);
    }

    @GetMapping("/{userId}/membership")
    public Membership getMembership(@PathVariable Long userId) {
        return userService.getMembershipByUserId(userId);
    }

    @PostMapping("/{userId}/membership")
    public Membership addOrUpdateMembership(@PathVariable Long userId, @RequestBody Membership membership) {
        return membershipService.saveMembershipForUser(userId, membership);
    }
}




