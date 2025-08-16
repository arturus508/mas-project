package com.example.healthfitness.service;

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

    public Habit save(Habit h){
        if (h.getCadence() == null || h.getCadence().isBlank()) h.setCadence("DAILY");
        if (h.getTargetPerDay() == null || h.getTargetPerDay() < 1) h.setTargetPerDay(1);
        return habitRepository.save(h);
    }

    public Habit getOrThrow(Long id){
        return habitRepository.findById(id).orElseThrow();
    }

    public void delete(Habit h){
        habitRepository.delete(h);
    }
}
