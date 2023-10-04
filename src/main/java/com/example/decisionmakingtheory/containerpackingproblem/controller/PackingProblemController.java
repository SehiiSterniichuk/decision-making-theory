package com.example.decisionmakingtheory.containerpackingproblem.controller;

import com.example.decisionmakingtheory.containerpackingproblem.domain.ExplorationStatistics;
import com.example.decisionmakingtheory.containerpackingproblem.service.PackingProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/packing-problem")
@RequiredArgsConstructor
public class PackingProblemController {
    private final PackingProblemService service;
    @GetMapping
    public String getDefaultExploration(Model model){
        ExplorationStatistics statistics = service.makeDefaultExploration();
        model.addAttribute("statistics", statistics);
        return "packingproblem";
    }
}
