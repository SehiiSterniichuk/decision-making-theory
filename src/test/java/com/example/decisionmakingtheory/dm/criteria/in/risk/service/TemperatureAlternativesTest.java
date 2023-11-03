package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.Alternative;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TemperatureAlternativesTest {

    @Test
    void getAlternativeForTemperature() {
        List<Alternative> list = IntStream.range(-1, 5)
                .mapToObj(v -> Alternative.builder()
                        .min((byte) (v * 10 + 1))
                        .max((byte) ((v + 1) * 10)).build()).toList();
        TemperatureAlternatives map = new TemperatureAlternatives(list);
        Alternative a = map.getAlternativeForTemperature(-5);
        log.info(a.toString());
        assertEquals(a.min(), -9);
        assertEquals(a.max(), 0);
        a = map.getAlternativeForTemperature(5);
        log.info(a.toString());
        assertEquals(a.min(), 1);
        assertEquals(a.max(), 10);
        a = map.getAlternativeForTemperature(10);
        log.info(a.toString());
        assertEquals(a.min(), 1);
        assertEquals(a.max(), 10);
        a = map.getAlternativeForTemperature(-9);
        log.info(a.toString());
        assertEquals(a.min(), -9);
        assertEquals(a.max(), 0);
    }
}