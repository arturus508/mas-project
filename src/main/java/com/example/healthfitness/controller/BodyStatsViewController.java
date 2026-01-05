package com.example.healthfitness.controller;

import com.example.healthfitness.service.BodyStatsService;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.web.form.BodyStatsForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * MVC controller for recording and viewing body statistics.  This
 * implementation follows a strict Controller → Service → Repository
 * layering: it performs request mapping, validation and DTO
 * binding, delegates domain logic to the service layer and
 * prepares the model for the view.  It does not directly access
 * persistence or contain business logic.
 */
@Controller
@RequestMapping("/body-stats")
public class BodyStatsViewController {

    private final BodyStatsService bodyStatsService;
    private final CurrentUserService currentUserService;
    private final UserService userService;

    public BodyStatsViewController(BodyStatsService bodyStatsService,
                                   CurrentUserService currentUserService,
                                   UserService userService) {
        this.bodyStatsService    = bodyStatsService;
        this.currentUserService  = currentUserService;
        this.userService         = userService;
    }

    /**
     * List the body statistics for the currently logged in user and
     * present an empty form for adding a new entry.  The controller
     * does not resolve the user from the security context directly;
     * instead it uses {@link CurrentUserService} to obtain the id of
     * the authenticated user.
     */
    @GetMapping
    public String showBodyStats(Model model) {
        Long userId = currentUserService.id();
        List<com.example.healthfitness.model.BodyStats> stats = bodyStatsService.getBodyStatsByUser(userId);
        addChartData(model, stats);
        model.addAttribute("bodyStats", stats);
        BodyStatsForm form = new BodyStatsForm();
        LocalDate today = LocalDate.now();
        form.setDateRecorded(today);
        boolean exists = false;
        var existingToday = bodyStatsService.getBodyStatsForDate(userId, today);
        if (existingToday.isPresent()) {
            exists = true;
            var existing = existingToday.get();
            form.setWeight(existing.getWeight());
            form.setBodyFatPercent(existing.getBodyFatPercent());
        }
        model.addAttribute("bodyStatsForm", form);
        model.addAttribute("bodyStatsUpdate", exists);
        return "body-stats";
    }

    /**
     * Process submission of a body stats form.  Validation errors
     * cause the list page to be re-rendered with existing stats and
     * field error messages.  On success the stats are recorded via
     * the service layer and the user is redirected back to the list.
     */
    @PostMapping("/add")
    public String addBodyStats(@Valid @ModelAttribute("bodyStatsForm") BodyStatsForm form,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            // Re-display existing stats when validation fails
            Long userId = currentUserService.id();
            List<com.example.healthfitness.model.BodyStats> stats = bodyStatsService.getBodyStatsByUser(userId);
            addChartData(model, stats);
            model.addAttribute("bodyStats", stats);
            boolean exists = false;
            if (form.getDateRecorded() != null) {
                exists = bodyStatsService.getBodyStatsForDate(userId, form.getDateRecorded()).isPresent();
            }
            model.addAttribute("bodyStatsUpdate", exists);
            return "body-stats";
        }
        Long userId = currentUserService.id();
        bodyStatsService.addBodyStatsToUser(userId, form);
        return "redirect:/body-stats";
    }

    /**
     * Delete a body stats entry for the current user.  Although HTTP
     * DELETE would be more semantically appropriate, Thymeleaf forms
     * often post back via POST with a hidden `_method` field.  This
     * simple mapping assumes the delete link or form uses POST.
     */
    @PostMapping("/delete/{id}")
    public String deleteBodyStats(@PathVariable("id") Long id) {
        Long userId = currentUserService.id();
        bodyStatsService.deleteBodyStats(userId, id);
        return "redirect:/body-stats";
    }

    private void addChartData(Model model, List<com.example.healthfitness.model.BodyStats> stats) {
        List<com.example.healthfitness.model.BodyStats> sorted = new ArrayList<>(stats);
        sorted.sort(Comparator.comparing(com.example.healthfitness.model.BodyStats::getDateRecorded));
        List<String> dates = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        for (com.example.healthfitness.model.BodyStats s : sorted) {
            dates.add(s.getDateRecorded() == null ? "" : s.getDateRecorded().toString());
            weights.add(s.getWeight());
        }
        model.addAttribute("statsDates", dates);
        model.addAttribute("statsWeights", weights);
    }
}
