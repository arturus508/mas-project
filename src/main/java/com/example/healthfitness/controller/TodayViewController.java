package com.example.healthfitness.controller;

import com.example.healthfitness.model.Habit;
import com.example.healthfitness.model.Meal;
import com.example.healthfitness.model.User;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.BodyStatsService;
import com.example.healthfitness.service.DailyReviewService;
import com.example.healthfitness.service.HabitLogService;
import com.example.healthfitness.service.HabitService;
import com.example.healthfitness.service.DailyWorkoutService;
import com.example.healthfitness.service.SectionVisibilityService;
import com.example.healthfitness.service.SleepService;
import com.example.healthfitness.service.TaskService;
import com.example.healthfitness.service.TodayViewService;
import com.example.healthfitness.service.WeeklyViewService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.MealMacroService;
import com.example.healthfitness.model.MealType;
import com.example.healthfitness.web.form.BodyStatsForm;
import com.example.healthfitness.web.form.DailyReviewForm;
import com.example.healthfitness.web.form.FlowTaskForm;
import com.example.healthfitness.web.form.SleepForm;
import com.example.healthfitness.web.view.TodayViewModel;
import com.example.healthfitness.web.view.WeeklyViewModel;
import com.example.healthfitness.web.view.HiddenSectionItem;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/today")
public class TodayViewController {

    private final TodayViewService todayViewService;
    private final CurrentUserService currentUserService;
    private final TaskService taskService;
    private final HabitService habitService;
    private final HabitLogService habitLogService;
    private final UserService userService;
    private final DailyReviewService dailyReviewService;
    private final WeeklyViewService weeklyViewService;
    private final DailyWorkoutService dailyWorkoutService;
    private final BodyStatsService bodyStatsService;
    private final SleepService sleepService;
    private final SectionVisibilityService sectionVisibilityService;
    private final MealMacroService mealMacroService;

    public TodayViewController(TodayViewService todayViewService,
                               CurrentUserService currentUserService,
                               TaskService taskService,
                               HabitService habitService,
                               HabitLogService habitLogService,
                               UserService userService,
                               DailyReviewService dailyReviewService,
                               WeeklyViewService weeklyViewService,
                               DailyWorkoutService dailyWorkoutService,
                               BodyStatsService bodyStatsService,
                               SleepService sleepService,
                               SectionVisibilityService sectionVisibilityService,
                               MealMacroService mealMacroService) {
        this.todayViewService = todayViewService;
        this.currentUserService = currentUserService;
        this.taskService = taskService;
        this.habitService = habitService;
        this.habitLogService = habitLogService;
        this.userService = userService;
        this.dailyReviewService = dailyReviewService;
        this.weeklyViewService = weeklyViewService;
        this.dailyWorkoutService = dailyWorkoutService;
        this.bodyStatsService = bodyStatsService;
        this.sleepService = sleepService;
        this.sectionVisibilityService = sectionVisibilityService;
        this.mealMacroService = mealMacroService;
    }

    @GetMapping
    public String today(@RequestParam(value = "mode", required = false) String mode,
                        @RequestParam(value = "range", required = false) String range,
                        @RequestParam(value = "date", required = false) String dateParam,
                        Model model) {
        HubParams params = normalizeParams(mode, range, dateParam);
        if (params.changed) {
            return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
        }
        if ("week".equals(params.range)) {
            return week(params, model);
        }
        return day(params, model);
    }

    @GetMapping("/{date}")
    public String day(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      Model model) {
        return "redirect:/today?mode=body&range=today&date=" + date;
    }

    @PostMapping("/{date}/tasks/{id}/toggle")
    public String toggleTask(@PathVariable Long id,
                             @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             @RequestParam(value = "mode", required = false) String mode,
                             @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        taskService.toggle(userId, id);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/habits/{id}/toggle")
    public String toggleHabit(@PathVariable Long id,
                              @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam(value = "mode", required = false) String mode,
                              @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        User user = userService.getUserById(userId);
        Habit habit = habitService.getForUserOrThrow(userId, id);
        habitLogService.toggle(user, habit, date);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/review")
    public String saveReview(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             @RequestParam(value = "mode", required = false) String mode,
                             @RequestParam(value = "range", required = false) String range,
                             @Valid DailyReviewForm dailyReviewForm,
                             BindingResult bindingResult,
                             Model model) {
        Long userId = currentUserService.id();
        if (bindingResult.hasErrors()) {
            TodayViewModel vm = todayViewService.build(userId, date);
            model.addAttribute("vm", vm);
            model.addAttribute("dailyReviewForm", dailyReviewForm);
            HubParams params = normalizeParams(mode, range, date.toString());
            model.addAttribute("hubMode", params.mode);
            model.addAttribute("hubRange", params.range);
            model.addAttribute("hubDate", params.date);
            return "today";
        }
        dailyReviewService.saveReview(userId, date, dailyReviewForm);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/workout/{workoutId}/sets/add")
    public String addWorkoutSet(@PathVariable Long workoutId,
                                @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                @RequestParam Long exerciseId,
                                @RequestParam(value = "mode", required = false) String mode,
                                @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        dailyWorkoutService.addSet(userId, workoutId, exerciseId);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/workout/add")
    public String addWorkoutFromPlan(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                     @RequestParam Long planDayId,
                                     @RequestParam(value = "mode", required = false) String mode,
                                     @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        dailyWorkoutService.getOrCreateFromPlanDay(userId, planDayId, date);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/workout/delete")
    public String deleteWorkout(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                @RequestParam(value = "mode", required = false) String mode,
                                @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        dailyWorkoutService.deleteWorkoutForDate(userId, date);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/workout/sets/{setId}/reps")
    public String updateWorkoutReps(@PathVariable Long setId,
                                    @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    @RequestParam(required = false) Integer repsDone,
                                    @RequestParam(value = "mode", required = false) String mode,
                                    @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        dailyWorkoutService.updateReps(userId, setId, repsDone);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/workout/sets/{setId}/delete")
    public String deleteWorkoutSet(@PathVariable Long setId,
                                   @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam(value = "mode", required = false) String mode,
                                   @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        dailyWorkoutService.deleteSet(userId, setId);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/body-stats/add")
    public String addBodyStats(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               @RequestParam Double weight,
                               @RequestParam(required = false) Double bodyFatPercent,
                               @RequestParam(value = "mode", required = false) String mode,
                               @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        BodyStatsForm form = new BodyStatsForm();
        form.setDateRecorded(date);
        form.setWeight(weight);
        form.setBodyFatPercent(bodyFatPercent);
        bodyStatsService.addBodyStatsToUser(userId, form);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/tasks/add")
    public String addTask(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam String title,
                          @RequestParam(value = "mode", required = false) String mode,
                          @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        if (title == null || title.trim().isEmpty()) {
            HubParams params = normalizeParams(mode, range, date.toString());
            return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
        }
        FlowTaskForm form = new FlowTaskForm();
        form.setDate(date);
        form.setTitle(title.trim());
        form.setPriority("MEDIUM");
        taskService.create(userId, form);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/tasks/copy-yesterday")
    public String copyTasksFromYesterday(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                         @RequestParam(value = "mode", required = false) String mode,
                                         @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        taskService.copyFromDate(userId, date.minusDays(1), date);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/sleep/add")
    public String addSleep(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           @RequestParam Integer hours,
                           @RequestParam Integer minutes,
                           @RequestParam Integer quality,
                           @RequestParam(value = "mode", required = false) String mode,
                           @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        SleepForm form = new SleepForm();
        form.setDate(date.minusDays(1));
        form.setHours(hours);
        form.setMinutes(minutes);
        form.setQuality(quality);
        sleepService.create(userId, form);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    @PostMapping("/{date}/meals/add-food")
    public String addMealFood(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam String segment,
                              @RequestParam Long foodItemId,
                              @RequestParam Integer quantity,
                              @RequestParam(value = "mode", required = false) String mode,
                              @RequestParam(value = "range", required = false) String range) {
        Long userId = currentUserService.id();
        MealType type = mealTypeFromSegment(segment);
        Meal meal = mealMacroService.getOrCreateMeal(userId, date, type);
        mealMacroService.addItem(userId, meal.getMealId(), foodItemId, quantity);
        HubParams params = normalizeParams(mode, range, date.toString());
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    private DailyReviewForm buildReviewForm(TodayViewModel vm) {
        DailyReviewForm form = new DailyReviewForm();
        if (vm != null && vm.isReviewPresent()) {
            form.setMood(vm.getReviewMood());
            form.setEnergy(vm.getReviewEnergy());
            form.setNote(vm.getReviewNote());
            form.setResetDone(vm.isReviewResetDone());
        }
        return form;
    }

    private MealType mealTypeFromSegment(String segment) {
        if (segment == null) {
            throw new IllegalArgumentException("Segment is required");
        }
        String key = segment.trim().toUpperCase(java.util.Locale.ROOT);
        if (key.endsWith("S")) {
            key = key.substring(0, key.length() - 1);
        }
        return MealType.valueOf(key);
    }

    private String day(HubParams params, Model model) {
        Long userId = currentUserService.id();
        TodayViewModel vm = todayViewService.build(userId, params.date);
        model.addAttribute("vm", vm);
        model.addAttribute("dailyReviewForm", buildReviewForm(vm));
        model.addAttribute("hubMode", params.mode);
        model.addAttribute("hubRange", params.range);
        model.addAttribute("hubDate", params.date);
        addVisibilityModel(userId, params, model);
        return "today";
    }

    private String week(HubParams params, Model model) {
        Long userId = currentUserService.id();
        WeeklyViewModel vm = weeklyViewService.build(userId, params.date);
        model.addAttribute("vm", vm);
        model.addAttribute("hubMode", params.mode);
        model.addAttribute("hubRange", params.range);
        model.addAttribute("hubDate", params.date);
        addVisibilityModel(userId, params, model);
        return "weekly";
    }

    @PostMapping("/sections/{key}/visibility")
    public String updateSectionVisibility(@PathVariable String key,
                                          @RequestParam boolean hidden,
                                          @RequestParam(value = "mode", required = false) String mode,
                                          @RequestParam(value = "range", required = false) String range,
                                          @RequestParam(value = "date", required = false) String dateParam) {
        Long userId = currentUserService.id();
        sectionVisibilityService.setHidden(userId, key, hidden);
        HubParams params = normalizeParams(mode, range, dateParam);
        return "redirect:/today?mode=" + params.mode + "&range=" + params.range + "&date=" + params.date;
    }

    private void addVisibilityModel(Long userId, HubParams params, Model model) {
        var hiddenMap = sectionVisibilityService.getHiddenMap(userId);
        model.addAttribute("hiddenSections", hiddenMap);
        List<HiddenSectionItem> hiddenItems =
                sectionVisibilityService.getHiddenItemsFor(params.mode, params.range, hiddenMap.keySet());
        model.addAttribute("hiddenItems", hiddenItems);
    }

    private HubParams normalizeParams(String modeParam, String rangeParam, String dateParam) {
        String mode = sanitize(modeParam, Set.of("body", "mind"), "body");
        String range = sanitize(rangeParam, Set.of("today", "week"), "today");
        LocalDate date = parseDate(dateParam, LocalDate.now());
        boolean changed = !mode.equals(nullToEmpty(modeParam))
                || !range.equals(nullToEmpty(rangeParam))
                || (dateParam == null || dateParam.isBlank() || !dateParam.equals(date.toString()));
        return new HubParams(mode, range, date, changed);
    }

    private String sanitize(String value, Set<String> allowed, String fallback) {
        if (value == null) {
            return fallback;
        }
        String normalized = value.trim().toLowerCase();
        return allowed.contains(normalized) ? normalized : fallback;
    }

    private LocalDate parseDate(String value, LocalDate fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException ex) {
            return fallback;
        }
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private static class HubParams {
        private final String mode;
        private final String range;
        private final LocalDate date;
        private final boolean changed;

        private HubParams(String mode, String range, LocalDate date, boolean changed) {
            this.mode = mode;
            this.range = range;
            this.date = date;
            this.changed = changed;
        }
    }
}
