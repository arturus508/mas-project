package com.example.healthfitness.controller;

import com.example.healthfitness.model.GuideCategory;
import com.example.healthfitness.service.GuideFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flow/guides")
public class FlowGuideController {

    @Autowired private GuideFlowService guideFlowService;

    @GetMapping
    public String list(@RequestParam(required = false) GuideCategory category , Model model){
        model.addAttribute("categories", GuideCategory.values());
        model.addAttribute("selected", category);
        model.addAttribute("guides", guideFlowService.listAll(category));
        return "guides";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title ,
                      @RequestParam GuideCategory category ,
                      @RequestParam String content ,
                      @RequestParam(required = false) String mediaUrl){
        guideFlowService.add(title, category, content, mediaUrl);
        return "redirect:/flow/guides";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        guideFlowService.delete(id);
        return "redirect:/flow/guides";
    }
}
