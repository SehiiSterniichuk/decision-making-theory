package com.example.decisionmakingtheory.dm.criteria.in.risk.controller;

import com.example.decisionmakingtheory.dm.criteria.in.risk.service.RiskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/decision-making-in-risk-conditions")
@RequiredArgsConstructor
@Slf4j
public class RiskController {
    private final RiskService service;

    @GetMapping("/strategies/{coeff}/{priceDivisor}")
    public String getStrategies(Model model, @PathVariable float coeff, @PathVariable float priceDivisor) {
        var response = service.getResponse(coeff, priceDivisor);
        model.addAttribute("table", response.table());
        model.addAttribute("discountTable", response.discountTable());
        model.addAttribute("weatherData", response.weatherData());
        model.addAttribute("strategiesResponse", response.strategyBodies());
        return "risk_page";
    }
}