package com.example.healthfitness.service;

import com.example.healthfitness.model.DailyReview;
import com.example.healthfitness.model.DailyWorkoutSet;
import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.HabitLog;
import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.model.Task;
import com.example.healthfitness.model.User;
import com.example.healthfitness.model.BodyStats;
import com.example.healthfitness.repository.DailyReviewRepository;
import com.example.healthfitness.repository.DailyWorkoutRepository;
import com.example.healthfitness.repository.DailyWorkoutSetRepository;
import com.example.healthfitness.repository.HabitLogRepository;
import com.example.healthfitness.repository.SleepEntryRepository;
import com.example.healthfitness.repository.TaskRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.view.WeeklyViewModel;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeeklyViewService {

    private final UserRepository userRepository;
    private final MealMacroService mealMacroService;
    private final DailyWorkoutRepository dailyWorkoutRepository;
    private final DailyWorkoutSetRepository dailyWorkoutSetRepository;
    private final SleepEntryRepository sleepEntryRepository;
    private final TaskRepository taskRepository;
    private final HabitService habitService;
    private final HabitLogRepository habitLogRepository;
    private final DailyReviewRepository dailyReviewRepository;
    private final BodyStatsService bodyStatsService;

    public WeeklyViewService(UserRepository userRepository,
                             MealMacroService mealMacroService,
                             DailyWorkoutRepository dailyWorkoutRepository,
                             DailyWorkoutSetRepository dailyWorkoutSetRepository,
                             SleepEntryRepository sleepEntryRepository,
                             TaskRepository taskRepository,
                             HabitService habitService,
                             HabitLogRepository habitLogRepository,
                             DailyReviewRepository dailyReviewRepository,
                             BodyStatsService bodyStatsService) {
        this.userRepository = userRepository;
        this.mealMacroService = mealMacroService;
        this.dailyWorkoutRepository = dailyWorkoutRepository;
        this.dailyWorkoutSetRepository = dailyWorkoutSetRepository;
        this.sleepEntryRepository = sleepEntryRepository;
        this.taskRepository = taskRepository;
        this.habitService = habitService;
        this.habitLogRepository = habitLogRepository;
        this.dailyReviewRepository = dailyReviewRepository;
        this.bodyStatsService = bodyStatsService;
    }

    public WeeklyViewModel build(Long userId, LocalDate anchorDate) {
        LocalDate date = anchorDate == null ? LocalDate.now() : anchorDate;
        LocalDate start = date.with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(6);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.example.healthfitness.exception.ResourceNotFoundException("User not found with id: " + userId));

        WeeklyViewModel vm = new WeeklyViewModel();
        vm.setWeekStart(start);
        vm.setWeekEnd(end);

        int kcal = 0;
        int protein = 0;
        int fat = 0;
        int carbs = 0;
        for (int i = 0; i < 7; i++) {
            LocalDate d = start.plusDays(i);
            Map<String, Integer> totals = mealMacroService.totalsForDay(userId, d);
            kcal += totals.getOrDefault("kcal", 0);
            protein += totals.getOrDefault("protein", 0);
            fat += totals.getOrDefault("fat", 0);
            carbs += totals.getOrDefault("carbs", 0);
        }
        vm.setAvgKcal(kcal / 7);
        vm.setAvgProtein(protein / 7);
        vm.setAvgFat(fat / 7);
        vm.setAvgCarbs(carbs / 7);

        List<SleepEntry> sleeps = sleepEntryRepository.findByUserAndDateBetweenOrderByDateAsc(user, start, end);
        if (!sleeps.isEmpty()) {
            long minutes = 0;
            int qualitySum = 0;
            int qualityCount = 0;
            for (SleepEntry e : sleeps) {
                minutes += durationMinutes(e);
                if (e.getQuality() != null) {
                    qualitySum += e.getQuality();
                    qualityCount++;
                }
            }
            vm.setAvgSleepMinutes((int) (minutes / sleeps.size()));
            if (qualityCount > 0) {
                vm.setAvgSleepQuality(qualitySum / qualityCount);
            }
        }

        int workoutsCount = dailyWorkoutRepository.findByUser_UserIdAndDateBetween(userId, start, end).size();
        vm.setWorkoutsCount(workoutsCount);

        Map<String, Integer> muscleGroups = new HashMap<>();
        List<DailyWorkoutSet> sets = dailyWorkoutSetRepository.findByUserAndDateBetweenWithExercise(userId, start, end);
        for (DailyWorkoutSet set : sets) {
            if (set.getExercise() == null || set.getExercise().getMuscleGroup() == null) {
                continue;
            }
            String key = set.getExercise().getMuscleGroup().trim();
            if (key.isEmpty()) {
                continue;
            }
            muscleGroups.put(key, muscleGroups.getOrDefault(key, 0) + 1);
        }
        vm.setMuscleGroupCounts(muscleGroups);

        List<Task> tasks = taskRepository.findByUserAndDateBetweenOrderByDateAsc(user, start, end);
        vm.setTasksTotal(tasks.size());
        vm.setTasksDone(tasks.stream().filter(Task::isDone).count());

        List<Habit> habits = habitService.findActiveByUser(user);
        List<HabitLog> logs = habitLogRepository.findByUserAndDateBetweenOrderByDateAsc(user, start, end);
        java.util.Map<Long, Long> counts = logs.stream()
                .filter(l -> Boolean.TRUE.equals(l.getDone()))
                .collect(java.util.stream.Collectors.groupingBy(l -> l.getHabit().getId(), java.util.stream.Collectors.counting()));
        int habitsDone = 0;
        int habitsPossible = 0;
        for (Habit h : habits) {
            String cadence = h.getCadence() == null ? "DAILY" : h.getCadence().toUpperCase();
            if ("WEEKLY".equals(cadence)) {
                int target = h.getTargetPerWeek() != null ? h.getTargetPerWeek() : 1;
                int done = counts.getOrDefault(h.getId(), 0L).intValue();
                habitsDone += Math.min(done, target);
                habitsPossible += target;
            } else {
                int done = counts.getOrDefault(h.getId(), 0L).intValue();
                habitsDone += Math.min(done, 7);
                habitsPossible += 7;
            }
        }
        vm.setHabitsDone(habitsDone);
        vm.setHabitsPossible(habitsPossible);

        List<DailyReview> reviews = dailyReviewRepository.findByUser_UserIdAndDateBetween(userId, start, end);
        vm.setReviewsCount(reviews.size());
        int moodSum = 0;
        int moodCount = 0;
        int energySum = 0;
        int energyCount = 0;
        for (DailyReview r : reviews) {
            if (r.getMood() != null) {
                moodSum += r.getMood();
                moodCount++;
            }
            if (r.getEnergy() != null) {
                energySum += r.getEnergy();
                energyCount++;
            }
        }
        if (moodCount > 0) {
            vm.setAvgMood(moodSum / moodCount);
        }
        if (energyCount > 0) {
            vm.setAvgEnergy(energySum / energyCount);
        }

        List<BodyStats> stats = bodyStatsService.getBodyStatsByUser(userId);
        if (!stats.isEmpty()) {
            List<BodyStats> weekStats = stats.stream()
                    .filter(s -> s.getDateRecorded() != null
                            && !s.getDateRecorded().isBefore(start)
                            && !s.getDateRecorded().isAfter(end))
                    .sorted(java.util.Comparator.comparing(BodyStats::getDateRecorded))
                    .toList();
            if (!weekStats.isEmpty()) {
                BodyStats first = weekStats.get(0);
                BodyStats last = weekStats.get(weekStats.size() - 1);
                vm.setWeightStart(first.getWeight());
                vm.setWeightEnd(last.getWeight());
                vm.setWeightDates(weekStats.stream()
                        .map(s -> s.getDateRecorded().toString())
                        .toList());
                vm.setWeightValues(weekStats.stream()
                        .map(BodyStats::getWeight)
                        .toList());
            }
        }

        return vm;
    }

    private long durationMinutes(SleepEntry e) {
        var start = e.getSleepStart();
        var end = e.getSleepEnd();
        if (start == null || end == null) {
            return 0;
        }
        if (end.isBefore(start)) {
            end = end.plusDays(1);
        }
        return java.time.Duration.between(start, end).toMinutes();
    }
}
