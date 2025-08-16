package com.example.healthfitness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/flow/stress" , "/mind/stress-relief" , "/stress"})
public class StressReliefController {

    @GetMapping
    public String view(Model model){
        return "stress-relief";
    }
}
