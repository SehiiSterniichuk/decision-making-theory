package com.example.decisionmakingtheory.bootstrap;

import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.implementation.FileServiceImpl;
import com.example.decisionmakingtheory.votingmethods.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootStrapRunner implements CommandLineRunner {
    private final VotingService service;

    private final FileServiceImpl fileService;
    @Override
    public void run(String... args) {
        fileService.deleteOldOutput();
        service.defaultScenario("election.csv");
    }
}
