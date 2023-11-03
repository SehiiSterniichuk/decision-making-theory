package com.example.decisionmakingtheory.dm.criteria.in.risk.controller;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.PriceAlternativeTable;
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
    @GetMapping("/{coeff}")
    public String getEstimationTable(Model model, @PathVariable float coeff){
        PriceAlternativeTable priceAlternativeTable = service.getEstimationTable(coeff);
        model.addAttribute("table", priceAlternativeTable);
        return "estimation-table";
    }
}