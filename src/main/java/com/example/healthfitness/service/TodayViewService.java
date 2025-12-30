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
    private final DailyReviewService dailyReviewService;

    public TodayViewService(UserRepository userRepository,
                            TaskRepository taskRepository,
                            MealMacroService mealMacroService,
                            DailyWorkoutService dailyWorkoutService,
                            SleepService sleepService,
                            HabitService habitService,
                            HabitLogService habitLogService,
                            DailyReviewService dailyReviewService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.mealMacroService = mealMacroService;
        this.dailyWorkoutService = dailyWorkoutService;
        this.sleepService = sleepService;
        this.habitService = habitService;
        this.habitLogService = habitLogService;
        this.dailyReviewService = dailyReviewService;
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

        List<Task> top = taskRepository.findByUserAndDateAndTopTrueOrderByIdAsc(user, d);
        List<Task> source = top.isEmpty() ? tasks : top;
        vm.setTopTasks(source.stream()
                .limit(3)
                .map(t -> new TodayTaskItem(t.getId(), t.getTitle(), t.isDone()))
                .toList());

        List<Habit> habits = habitService.findActiveByUser(user);
        vm.setHabitsTotal(habits.size());
        var doneMap = habitLogService.mapForDate(user, d);
        LocalDate weekStart = d.with(java.time.DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);
        List<HabitLog> weekLogs = habitLogService.listForDateRange(user, weekStart, weekEnd);
        java.util.Map<Long, Long> weekCounts = weekLogs.stream()
                .collect(java.util.stream.Collectors.groupingBy(l -> l.getHabit().getId(), java.util.stream.Collectors.counting()));

        int doneHabits = 0;
        java.util.List<TodayHabitItem> items = new java.util.ArrayList<>();
        for (Habit h : habits) {
            String cadence = h.getCadence() == null ? "DAILY" : h.getCadence().toUpperCase();
            if ("WEEKLY".equals(cadence)) {
                int target = h.getTargetPerWeek() != null ? h.getTargetPerWeek() : 1;
                int done = weekCounts.getOrDefault(h.getId(), 0L).intValue();
                boolean complete = done >= target;
                if (complete) {
                    doneHabits++;
                }
                items.add(new TodayHabitItem(h.getId(), h.getName(), complete, cadence, done, target));
            } else {
                boolean done = Boolean.TRUE.equals(doneMap.get(h.getId()));
                if (done) {
                    doneHabits++;
                }
                items.add(new TodayHabitItem(h.getId(), h.getName(), done, cadence, null, null));
            }
        }
        vm.setHabits(items);
        vm.setHabitsDone(doneHabits);

        dailyReviewService.findForDate(userId, d).ifPresent(review -> {
            vm.setReviewPresent(true);
            vm.setReviewMood(review.getMood());
            vm.setReviewEnergy(review.getEnergy());
            vm.setReviewNote(review.getNote());
            vm.setReviewResetDone(review.isResetDone());
        });

        return vm;
    }
}
