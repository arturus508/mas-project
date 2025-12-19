package com.example.healthfitness.controller;

import com.example.healthfitness.service.DreamService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.service.CurrentUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller for dream logging.  This version no longer uses
 * {@code SecurityContextHolder} to look up the authenticated user.  It
 * instead delegates to {@link CurrentUserService} for the current user id
 * and resolves the {@link com.example.healthfitness.model.User} via
 * {@link UserService}.  Query parameters for dates are automatically
 * converted via {@link DateTimeFormat}.
 */
@Controller
@RequestMapping("/mind/dreams")
public class DreamViewController {

    private final DreamService dreamService;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public DreamViewController(DreamService dreamService,
                               UserService userService,
                               CurrentUserService currentUserService) {
        this.dreamService = dreamService;
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String list(@RequestParam(value = "from", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                       @RequestParam(value = "to", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                       @RequestParam(value = "tag", required = false) String tag,
                       Model model) {
        Long userId = currentUserService.id();
        var user = userService.getUserById(userId);
        var items = dreamService.search(user.getUserId(), from, to, tag);
        model.addAttribute("dreams", items);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("tag", tag);
        return "dreams";
    }

    @PostMapping("/add")
    public String add(@RequestParam(value = "sleepEntryId", required = false) Long sleepEntryId,
                      @RequestParam("title")   String title,
                      @RequestParam("content") String content,
                      @RequestParam(value = "tags", required = false) String tags,
                      @RequestParam(value = "mood", required = false) Integer mood,
                      @RequestParam(value = "lucid", required = false)  Boolean lucid,
                      @RequestParam(value = "nightmare", required = false) Boolean nightmare,
                      @RequestParam(value = "date", required = false)
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = currentUserService.id();
        dreamService.add(userId, sleepEntryId, title, content, tags, mood, lucid, nightmare, date);
        return "redirect:/mind/dreams";
    }
}