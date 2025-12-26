package com.example.healthfitness.service;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.TaskRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.view.TodayHabitItem;
import com.example.healthfitness.web.view.TodayTaskItem;
import com.example.healthfitness.web.view.TodayViewModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TodayViewService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final MealMacroService mealMacroService;
    private final DailyWorkoutService dailyWorkoutService;
    private final SleepService sleepService;
    private final HabitService habitService;
    private final HabitLogService habitLogService;

    public TodayViewService(UserRepository userRepository,
                            TaskRepository taskRepository,
                            MealMacroService mealMacroService,
                            DailyWorkoutService dailyWorkoutService,
                            SleepService sleepService,
                            HabitService habitService,
                            HabitLogService habitLogService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.mealMacroService = mealMacroService;
        this.dailyWorkoutService = dailyWorkoutService;
        this.sleepService = sleepService;
        this.habitService = habitService;
        this.habitLogService = habitLogService;
    }

    public TodayViewModel build(Long userId, LocalDate date) {
        LocalDate d = date == null ? LocalDate.now() : date;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.example.healthfitness.exception.ResourceNotFoundException("User not found with id: " + userId));

        TodayViewModel vm = new TodayViewModel();
        vm.setDate(d);

        Map<String, Integer> mealTotals = mealMacroService.totalsForDay(userId, d);
        vm.setMealTotals(mealTotals);

        DailyWorkout workout = dailyWorkoutService.findForDate(userId, d);
        if (workout != null) {
            vm.setWorkoutTitle(workout.getTitle());
            List<DailyWorkoutSet> sets = dailyWorkoutService.getSets(userId, workout.getDailyWorkoutId());
            vm.setWorkoutSetsTotal(sets.size());
            int done = (int) sets.stream()
                    .filter(s -> s.getRepsDone() != null && s.getRepsDone() > 0)
                    .count();
            vm.setWorkoutSetsDone(done);
        }

        SleepEntry lastNight = sleepService.getByDate(userId, d.minusDays(1)).orElse(null);
        if (lastNight != null) {
            long minutes = sleepService.durationMinutes(lastNight);
            vm.setSleepHours((int) (minutes / 60));
            vm.setSleepMinutes((int) (minutes % 60));
            vm.setSleepQuality(lastNight.getQuality());
        }

        List<Task> tasks = taskRepository.findByUserAndDateOrderByIdAsc(user, d);
        vm.setTasksTotal(tasks.size());
        vm.setTasksDone((int) tasks.stream().filter(Task::isDone).count());
        vm.setTopTasks(tasks.stream()
                .limit(3)
                .map(t -> new TodayTaskItem(t.getId(), t.getTitle(), t.isDone()))
                .toList());

        List<Habit> habits = habitService.findByUser(user);
        vm.setHabitsTotal(habits.size());
        var doneMap = habitLogService.mapForDate(user, d);
        int doneHabits = 0;
        for (Habit h : habits) {
            if (Boolean.TRUE.equals(doneMap.get(h.getId()))) {
                doneHabits++;
            }
        }
        vm.setHabitsDone(doneHabits);
        vm.setHabits(habits.stream()
                .map(h -> new TodayHabitItem(
                        h.getId(),
                        h.getName(),
                        Boolean.TRUE.equals(doneMap.get(h.getId()))
                ))
                .toList());

        return vm;
    }
}
