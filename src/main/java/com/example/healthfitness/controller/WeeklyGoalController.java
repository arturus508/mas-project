package com.example.healthfitness.controller;

import com.example.healthfitness.service.AuthUserService;
import com.example.healthfitness.service.WeeklyReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeeklyGoalController {

    private final AuthUserService auth;
    private final WeeklyReviewService weekly;

    public WeeklyGoalController(AuthUserService auth, WeeklyReviewService weekly) {
        this.auth = auth;
        this.weekly = weekly;
    }

    @GetMapping("/flow/weekly-goals")
    public String weeklyGoals(Model model) {
        var user = auth.currentUser();
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
