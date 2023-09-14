package com.example.decisionmakingtheory.controller;

import com.example.decisionmakingtheory.domain.LabResult;
import com.example.decisionmakingtheory.services.Lab1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/v1/lab1")
@RequiredArgsConstructor
public class Lab1Controller {
    private final Lab1Service service;
    @GetMapping
    public String defaultPage(Model model) {
        List<LabResult> results = service.results();
        model.addAttribute("results", results);
        return "lab1";
    }
}
