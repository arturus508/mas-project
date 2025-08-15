package com.example.healthfitness.controller;

import com.example.healthfitness.service.DreamService;
import com.example.healthfitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/mind/dreams")
public class DreamViewController {

    @Autowired
    private DreamService dreamService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String list(@RequestParam(value = "from", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                       @RequestParam(value = "to", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                       @RequestParam(value = "tag", required = false) String tag,
                       Model model) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user  = userService.findByEmail(email).orElseThrow();
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
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user  = userService.findByEmail(email).orElseThrow();
        dreamService.add(user.getUserId(), sleepEntryId, title, content, tags, mood, lucid, nightmare, date);
        return "redirect:/mind/dreams";
    }
}
