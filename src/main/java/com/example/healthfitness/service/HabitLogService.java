package com.example.healthfitness.service;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.HabitLog;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.HabitLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HabitLogService {

    private final HabitLogRepository habitLogRepository;

    public HabitLogService(HabitLogRepository habitLogRepository) {
        this.habitLogRepository = habitLogRepository;
    }

    public Map<Long,Boolean> mapForDate(User user , LocalDate date) {
        LocalDate d = date == null ? LocalDate.now() : date;
        
        List<HabitLog> logs = habitLogRepository.findByUserAndDateBetweenOrderByDateAsc(user , d , d);
        Map<Long,Boolean> map = new HashMap<>();
        for (HabitLog l : logs) { map.put(l.getHabit().getId() , Boolean.TRUE); }
        return map;
    }

    public List<HabitLog> listForDateRange(User user, LocalDate from, LocalDate to) {
        LocalDate start = from == null ? LocalDate.now() : from;
        LocalDate end = to == null ? start : to;
        return habitLogRepository.findByUserAndDateBetweenOrderByDateAsc(user, start, end);
    }

    public long countDoneForHabitBetween(Habit habit, LocalDate from, LocalDate to) {
        LocalDate start = from == null ? LocalDate.now() : from;
        LocalDate end = to == null ? start : to;
        return habitLogRepository.countByHabitAndDateBetween(habit, start, end);
    }

    @Transactional
    public void toggle(User user , Habit habit , LocalDate date) {
        LocalDate d = date == null ? LocalDate.now() : date;
        var opt = habitLogRepository.findByHabitAndDate(habit , d);
        if (opt.isPresent()) {
            habitLogRepository.delete(opt.get());
            return;
        }
        HabitLog log = new HabitLog();
        log.setUser(user);
        log.setHabit(habit);
        log.setDate(d);
        log.setDone(true);
        log.setValue(null);
        habitLogRepository.save(log);
    }

    @Transactional
    public void deleteAllForHabit(Habit habit) {
        habitLogRepository.deleteByHabit(habit);
    }
}



