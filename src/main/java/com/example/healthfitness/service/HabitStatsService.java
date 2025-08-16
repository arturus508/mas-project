package com.example.healthfitness.service;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class HabitStatsService {

    private final HabitLogService habitLogService;

    public HabitStatsService(HabitLogService habitLogService){ this.habitLogService = habitLogService; }

    public List<LocalDate> windowDays(LocalDate ref , int days){
        List<LocalDate> out = new ArrayList<>(days);
        for (int i = days - 1 ; i >= 0 ; i--) out.add(ref.minusDays(i));
        return out;
    }

    public Map<LocalDate, Map<Long,Boolean>> mapsForDates(User u , List<LocalDate> days){
        Map<LocalDate, Map<Long,Boolean>> byDate = new LinkedHashMap<>();
        for (LocalDate d : days){
            Map<Long,Boolean> m = habitLogService.mapForDate(u , d);
            byDate.put(d , m == null ? Collections.emptyMap() : m);
        }
        return byDate;
    }

    public Map<Long,Integer> streaks(User u , List<Habit> habits , LocalDate ref , int lookback){
        List<LocalDate> days = windowDays(ref , lookback);
        Map<LocalDate, Map<Long,Boolean>> byDate = mapsForDates(u , days);
        Map<Long,Integer> res = new HashMap<>();

        for (Habit h : habits){
            int streak = 0; boolean started = false;
            for (int i = days.size()-1 ; i >= 0 ; i--){
                LocalDate d = days.get(i);
                boolean done = Optional.ofNullable(byDate.get(d).get(h.getId())).orElse(false);
                if (!started){
                    if (done){ started = true; streak = 1; }
                } else {
                    if (done) streak++; else break;
                }
            }
            res.put(h.getId() , streak);
        }
        return res;
    }

    public Map<Long, Map<String,Boolean>> heatForDays(List<Habit> habits , List<LocalDate> days ,
                                                      Map<LocalDate, Map<Long,Boolean>> byDate){
        Map<Long, Map<String,Boolean>> heat = new HashMap<>();
        for (Habit h : habits){
            Map<String,Boolean> inner = new LinkedHashMap<>();
            for (LocalDate d : days){
                boolean done = Optional.ofNullable(byDate.get(d).get(h.getId())).orElse(false);
                inner.put(d.toString() , done);
            }
            heat.put(h.getId() , inner);
        }
        return heat;
    }
}

