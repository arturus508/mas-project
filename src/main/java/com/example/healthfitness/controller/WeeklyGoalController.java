package com.example.healthfitness.controller;

import com.example.healthfitness.model.User;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.WeeklyReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for displaying weekly goals and summaries.  Instead of
 * relying on {@link com.example.healthfitness.service.AuthUserService}, this
 * implementation obtains the current user's id from
 * {@link CurrentUserService} and resolves the {@link User} via
 * {@link UserService}.  It then delegates to {@link WeeklyReviewService}
 * to compute tasks and habit summaries for the current week.
 */
@Controller
public class WeeklyGoalController {

    private final CurrentUserService currentUserService;
    private final UserService userService;
    private final WeeklyReviewService weekly;

    public WeeklyGoalController(CurrentUserService currentUserService,
                                UserService userService,
                                WeeklyReviewService weekly) {
        this.currentUserService = currentUserService;
        this.userService = userService;
        this.weekly = weekly;
    }

    @GetMapping("/flow/weekly-goals")
    public String weeklyGoals(Model model) {
        Long userId = currentUserService.id();
        User user = userService.getUserById(userId);
        var ww = weekly.currentWeekWindow();
        var tasks = weekly.tasksSummary(user, ww.start(), ww.end());
        var habits = weekly.habitsPlaceholder();
        var quote = weekly.quoteOfTheDay();
        model.addAttribute("weekStart", ww.start());
        model.addAttribute("weekEnd", ww.end());
        model.addAttribute("tasksDone", tasks.done());
        model.addAttribute("tasksTotal", tasks.total());
        model.addAttribute("tasksPct", tasks.percent());
        model.addAttribute("heat", habits.heat7());
        model.addAttribute("streak7", habits.streak7());
        model.addAttribute("quoteText", quote.text());
        model.addAttribute("quoteAuthor", quote.author());
        return "weekly-goals";
    }
}