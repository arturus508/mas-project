package com.example.healthfitness.service;

import com.example.healthfitness.model.*;
import com.example.healthfitness.repository.TaskRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.repository.FoodItemRepository;
import com.example.healthfitness.repository.WorkoutPlanDayRepository;
import com.example.healthfitness.web.view.TodayHabitItem;
import com.example.healthfitness.web.view.TodayFoodOption;
import com.example.healthfitness.web.view.TodayPlanDayOption;
import com.example.healthfitness.web.view.TodayTaskItem;
import com.example.healthfitness.web.view.TodayExerciseOption;
import com.example.healthfitness.web.view.TodayViewModel;
import com.example.healthfitness.web.view.TodayWorkoutSetItem;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class TodayViewService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final MealMacroService mealMacroService;
    private final DailyWorkoutService dailyWorkoutService;
    private final ExerciseService exerciseService;
    private final BodyStatsService bodyStatsService;
    private final SleepService sleepService;
    private final HabitService habitService;
    private final HabitLogService habitLogService;
    private final DailyReviewService dailyReviewService;
    private final WorkoutPlanDayRepository workoutPlanDayRepository;
    private final FoodItemRepository foodItemRepository;

    public TodayViewService(UserRepository userRepository,
                            TaskRepository taskRepository,
                            MealMacroService mealMacroService,
                            DailyWorkoutService dailyWorkoutService,
                            ExerciseService exerciseService,
                            BodyStatsService bodyStatsService,
                            SleepService sleepService,
                            HabitService habitService,
                            HabitLogService habitLogService,
                            DailyReviewService dailyReviewService,
                            WorkoutPlanDayRepository workoutPlanDayRepository,
                            FoodItemRepository foodItemRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.mealMacroService = mealMacroService;
        this.dailyWorkoutService = dailyWorkoutService;
        this.exerciseService = exerciseService;
        this.bodyStatsService = bodyStatsService;
        this.sleepService = sleepService;
        this.habitService = habitService;
        this.habitLogService = habitLogService;
        this.dailyReviewService = dailyReviewService;
        this.workoutPlanDayRepository = workoutPlanDayRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public TodayViewModel build(Long userId, LocalDate date) {
        LocalDate d = date == null ? LocalDate.now() : date;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.example.healthfitness.exception.ResourceNotFoundException("User not found with id: " + userId));

        TodayViewModel vm = new TodayViewModel();
        vm.setDate(d);

        Map<String, Integer> mealTotals = mealMacroService.totalsForDay(userId, d);
        vm.setMealTotals(mealTotals);
        vm.setMealFoodOptions(foodItemRepository.findTop20ByNameContainingIgnoreCaseOrderByNameAsc("")
                .stream()
                .map(f -> new TodayFoodOption(
                        f.getId(),
                        f.getName() + (f.getUnit() == Unit.G ? " (per 100g)" : " (per piece)")))
                .toList());

        DailyWorkout workout = dailyWorkoutService.findForDate(userId, d);
        if (workout != null) {
            vm.setWorkoutTitle(workout.getTitle());
            vm.setDailyWorkoutId(workout.getDailyWorkoutId());
            List<DailyWorkoutSet> sets = dailyWorkoutService.getSets(userId, workout.getDailyWorkoutId());
            sets.sort(Comparator
                    .comparing((DailyWorkoutSet s) -> s.getExercise() == null ? "" : s.getExercise().getExerciseName())
                    .thenComparing(DailyWorkoutSet::getSetNumber));
            vm.setWorkoutSetsTotal(sets.size());
            int done = (int) sets.stream()
                    .filter(s -> s.getRepsDone() != null && s.getRepsDone() > 0)
                    .count();
            vm.setWorkoutSetsDone(done);
            vm.setWorkoutSets(sets.stream()
                    .map(s -> new TodayWorkoutSetItem(
                            s.getDailyWorkoutSetId(),
                            s.getExercise() == null ? null : s.getExercise().getExerciseId(),
                            s.getExercise() == null ? "Exercise" : s.getExercise().getExerciseName(),
                            s.getSetNumber(),
                            s.getRepsDone()))
                    .toList());
            vm.setWorkoutExercises(exerciseService.getAllExercises().stream()
                    .sorted(Comparator.comparing(Exercise::getExerciseName, String.CASE_INSENSITIVE_ORDER))
                    .map(ex -> new TodayExerciseOption(ex.getExerciseId(), ex.getExerciseName()))
                    .toList());
        } else {
            List<WorkoutPlanDay> planDays = workoutPlanDayRepository
                    .findWithPlanByWorkoutPlan_User_UserIdOrderByWorkoutPlan_WorkoutPlanIdAscDayOrderAsc(userId);
            vm.setWorkoutPlanDays(planDays.stream()
                    .map(day -> {
                        String planName = day.getWorkoutPlan() != null ? day.getWorkoutPlan().getPlanName() : "Plan";
                        String dayName = (day.getName() == null || day.getName().isBlank())
                                ? "Day " + day.getDayOrder()
                                : day.getName();
                        return new TodayPlanDayOption(day.getPlanDayId(), planName + " — " + dayName);
                    })
                    .toList());
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

        List<Task> source = tasks.stream()
                .sorted(Comparator
                        .comparing(Task::isTop).reversed()
                        .thenComparing(Task::getId))
                .toList();
        vm.setTopTasks(source.stream()
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
                boolean doneToday = Boolean.TRUE.equals(doneMap.get(h.getId()));
                if (doneToday) {
                    doneHabits++;
                }
                items.add(new TodayHabitItem(h.getId(), h.getName(), doneToday, cadence, null, null));
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

        List<BodyStats> stats = bodyStatsService.getBodyStatsByUser(userId);
        List<String> weightDates = new ArrayList<>();
        List<Double> weightValues = new ArrayList<>();

        if (!stats.isEmpty()) {
            stats.sort(Comparator.comparing(BodyStats::getDateRecorded));
            BodyStats latest = stats.get(stats.size() - 1);
            vm.setLatestWeight(latest.getWeight());
            for (BodyStats s : stats) {
                if (s.getDateRecorded() != null && s.getDateRecorded().equals(d)) {
                    vm.setTodayWeight(s.getWeight());
                    vm.setTodayBodyFatPercent(s.getBodyFatPercent());
                    break;
                }
            }
            List<BodyStats> recent = stats.size() > 7 ? stats.subList(stats.size() - 7, stats.size()) : stats;
            recent.forEach(s -> {
                weightDates.add(s.getDateRecorded() == null ? "" : s.getDateRecorded().toString());
                weightValues.add(s.getWeight());
            });
        } else {
            for (int i = 0; i < 7; i++) {
                weightDates.add("");
                weightValues.add(0d);
            }
        }

        while (weightDates.size() < 7) {
            weightDates.add("");
            weightValues.add(0d);
        }

        vm.setWeightDates(weightDates);
        vm.setWeightValues(weightValues);

        return vm;
    }
}
