package com.example.decisionmakingtheory.bootstrap;

import com.example.decisionmakingtheory.config.Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootStrapRunner implements CommandLineRunner {
    private final Config config;
    @Override
    public void run(String... args) {
        log.info(config.getPathToCSV());
    }
}
