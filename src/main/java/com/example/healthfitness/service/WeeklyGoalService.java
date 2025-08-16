package com.example.healthfitness.service;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.WeeklyGoalRepository;
import com.example.healthfitness.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class WeeklyGoalService {

    @Autowired private WeeklyGoalRepository weeklyGoalRepository;
    @Autowired private UserRepository userRepository;

    public List<WeeklyGoal> listForUser(Long userId){
        var user = userRepository.findById(userId).orElseThrow();
        return weeklyGoalRepository.findByUserOrderByWeekStartDesc(user);
    }

    public WeeklyGoal add(Long userId , String title , WeeklyGoalType type ,
                          LocalDate weekStart , LocalDate weekEnd , Integer targetValue){
        var user = userRepository.findById(userId).orElseThrow();
        var g = new WeeklyGoal();
        g.setUser(user); g.setTitle(title); g.setType(type);
        g.setWeekStart(weekStart); g.setWeekEnd(weekEnd);
        g.setTargetValue(targetValue != null ? targetValue : 1);
        g.setCurrentValue(0);
        return weeklyGoalRepository.save(g);
    }

    public WeeklyGoal update(Long id , String title , WeeklyGoalType type ,
                             LocalDate weekStart , LocalDate weekEnd , Integer targetValue ,
                             Integer currentValue , Boolean completed){
        var g = weeklyGoalRepository.findById(id).orElseThrow();
        if (title != null) g.setTitle(title);
        if (type  != null) g.setType(type);
        if (weekStart != null) g.setWeekStart(weekStart);
        if (weekEnd   != null) g.setWeekEnd(weekEnd);
        if (targetValue != null) g.setTargetValue(targetValue);
        if (currentValue != null) g.setCurrentValue(Math.max(0, currentValue));
        if (completed != null && completed != g.isCompleted()){
            g.setCompleted(completed);
            g.setCompletedDate(completed ? LocalDate.now() : null);
        }
        return weeklyGoalRepository.save(g);
    }

    public void delete(Long id){ weeklyGoalRepository.deleteById(id); }

    public void increment(Long id , int by){
        var g = weeklyGoalRepository.findById(id).orElseThrow();
        g.setCurrentValue( Math.max(0, g.getCurrentValue() + by) );
        weeklyGoalRepository.save(g);
    }

    public void toggleComplete(Long id){
        var g = weeklyGoalRepository.findById(id).orElseThrow();
        var nd = !g.isCompleted();
        g.setCompleted(nd);
        g.setCompletedDate(nd ? LocalDate.now() : null);
        weeklyGoalRepository.save(g);
    }

    // auto weekly sumary
    public void finalizeFinishedWeeks(Long userId){
        var user = userRepository.findById(userId).orElseThrow();
        var now = LocalDate.now();
        var list = weeklyGoalRepository.findByUserOrderByWeekStartDesc(user);
        for (var g : list){
            if (!g.isCompleted() && now.isAfter(g.getWeekEnd()) && g.getCurrentValue() >= g.getTargetValue()){
                g.setCompleted(true);
                g.setCompletedDate(now);
                weeklyGoalRepository.save(g);
            }
        }
    }

    
    public static LocalDate firstDayOfThisWeek(){
        var d = LocalDate.now();
        var dow = d.getDayOfWeek().getValue(); // 1=Mon  ..  7=Sun
        return d.minusDays(dow - 1);
    }
    public static LocalDate lastDayOfThisWeek(){
        return firstDayOfThisWeek().plusDays(6);
    }
}
