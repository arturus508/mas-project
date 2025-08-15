package com.example.healthfitness.service;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.HabitCadence;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.HabitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public List<Habit> findByUser(User user) {
        return habitRepository.findByUserOrderByIdAsc(user);
    }

    public Habit getOrThrow(Long id) {
        return findByIdOrThrow(id);
    }

    public Habit findByIdOrThrow(Long id) {
        return habitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found  id=" + id));
    }

    @Transactional
    public Habit create(User user , String name , HabitCadence cadence , Integer targetPerDay) {
        Habit h = new Habit();
        h.setUser(user);
        h.setName(name);
        h.setCadence(cadence);
        h.setTargetPerDay(targetPerDay == null ? 1 : targetPerDay);
        return habitRepository.save(h);
    }

    @Transactional
    public void delete(Habit habit) {
        habitRepository.delete(habit);
    }

    @Transactional
    public void deleteById(Long id) {
        habitRepository.deleteById(id);
    }

    @Transactional
    public Habit save(Habit habit) {
        return habitRepository.save(habit);
    }
}

