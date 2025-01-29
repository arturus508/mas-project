package com.example.healthfitness.controller;

import com.example.healthfitness.service.BodyStatsService;
import com.example.healthfitness.model.BodyStats;
import com.example.healthfitness.repository.BodyStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/body-stats")
public class BodyStatsController {

    @Autowired
    private BodyStatsRepository bodyStatsRepository;

    @Autowired
    private BodyStatsService bodyStatsService;

    @GetMapping
    public List<BodyStats> getAllBodyStats() {
        return bodyStatsRepository.findAll();
    }

    @PostMapping
    public BodyStats createBodyStats(@RequestBody BodyStats bodyStats) {
        return bodyStatsRepository.save(bodyStats);
    }
}


