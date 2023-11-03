package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.Alternative;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AlternativeFactoryTest {
    private AlternativeFactory factory;

    @BeforeEach
    public void setUp() {
        Config config = new Config();
        config.setPathToClothesAlternatives("./input/alternatives.csv");
        factory = new AlternativeFactory(config);
    }
    @Test
    void getAlternatives() {
        List<Alternative> alternatives = factory.getAlternatives();
        assertFalse(alternatives.isEmpty());
        log.info(alternatives.get(0).toString());
    }
}