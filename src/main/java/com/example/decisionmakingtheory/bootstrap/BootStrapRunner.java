package com.example.decisionmakingtheory.bootstrap;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.StrategyResponse;
import com.example.decisionmakingtheory.dm.criteria.in.risk.service.RiskService;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.implementation.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootStrapRunner implements CommandLineRunner {
    private final RiskService service;
    private final FileServiceImpl fileService;
    @Override
    public void run(String... args) {
        fileService.deleteOldOutput();
        StrategyResponse response = service.getResponse(10);
        System.out.println(response);
    }
}
