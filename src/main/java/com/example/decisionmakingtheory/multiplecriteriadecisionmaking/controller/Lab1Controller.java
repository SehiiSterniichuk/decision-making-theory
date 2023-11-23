package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.controller;

import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.LabResult;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.Lab1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/test/{z1x1}/{z1x2}/{z2x1}/{z2x2}")
    public String testPage(Model model, @PathVariable Double z1x1, @PathVariable Double z1x2,
                           @PathVariable Double z2x1, @PathVariable Double z2x2,
                           @RequestParam double r, @RequestParam double R) {
        List<LabResult> results = service.testResults(z1x1, z1x2, z2x1, z2x2, r, R);
        model.addAttribute("results", results);
        return "lab1";
    }
}
