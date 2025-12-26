package com.example.healthfitness.controller;

import com.example.healthfitness.model.BodyStats;
import com.example.healthfitness.service.BodyStatsService;
import com.example.healthfitness.service.CurrentUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/body-stats")
public class BodyStatsController {

    private final BodyStatsService bodyStatsService;
    private final CurrentUserService currentUserService;

    public BodyStatsController(BodyStatsService bodyStatsService,
                               CurrentUserService currentUserService) {
        this.bodyStatsService = bodyStatsService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<BodyStats> getAllBodyStats() {
        Long userId = currentUserService.id();
        return bodyStatsService.getBodyStatsByUser(userId);
    }

    @PostMapping
    public BodyStats createBodyStats(@RequestBody BodyStats bodyStats) {
        Long userId = currentUserService.id();
        return bodyStatsService.addBodyStatsToUser(userId, bodyStats);
    }

    @DeleteMapping("/{id}")
    public void deleteBodyStats(@PathVariable Long id) {
        Long userId = currentUserService.id();
        bodyStatsService.deleteBodyStats(userId, id);
    }
}


