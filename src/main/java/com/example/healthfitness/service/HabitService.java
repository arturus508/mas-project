package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {

    @Autowired private HabitRepository habitRepository;

    public List<Habit> findByUser(User u){
        return habitRepository.findByUserOrderByIdAsc(u);
    }

    public List<Habit> findActiveByUser(User u) {
        return habitRepository.findByUserAndActiveTrueOrderByIdAsc(u);
    }

    public Habit save(Habit h){
        if (h.getCadence() == null || h.getCadence().isBlank()) h.setCadence("DAILY");
        if (h.getTargetPerDay() == null || h.getTargetPerDay() < 1) h.setTargetPerDay(1);
        if (h.getTargetPerWeek() != null && h.getTargetPerWeek() < 1) h.setTargetPerWeek(1);
        if ("WEEKLY".equalsIgnoreCase(h.getCadence()) && h.getTargetPerWeek() == null) {
            h.setTargetPerWeek(1);
        }
        return habitRepository.save(h);
    }

    public Habit getOrThrow(Long id){
        return habitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found with id: " + id));
    }

    public Habit getForUserOrThrow(Long userId, Long habitId) {
        Habit habit = getOrThrow(habitId);
        if (!habit.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Habit does not belong to current user");
        }
        return habit;
    }

    public void delete(Habit h){
        habitRepository.delete(h);
    }
}
