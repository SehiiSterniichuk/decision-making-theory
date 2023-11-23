package com.example.decisionmakingtheory.votingmethods.controller;

import com.example.decisionmakingtheory.votingmethods.domain.VotingResult;
import com.example.decisionmakingtheory.votingmethods.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/v1/voting")
@RequiredArgsConstructor
public class VotingController {
    private final VotingService service;
    @GetMapping("/{fileName}")
    public String defaultScenario(Model model, @PathVariable String fileName){
        List<VotingResult> results = service.defaultScenario(fileName);
        model.addAttribute("results", results);
        return "voting";
    }
}
