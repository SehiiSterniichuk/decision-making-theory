package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.Alternative;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.TreeMap;

@NoArgsConstructor
public class TemperatureAlternatives {
    private final TreeMap<Byte, Alternative> temperatureTree = new TreeMap<>();

    public TemperatureAlternatives(List<Alternative> alternatives) {
        alternatives.forEach(this::addAlternative);
    }

    public void addAlternative(Alternative alternative) {
        temperatureTree.put(alternative.min(), alternative);
    }

    public Alternative getAlternativeForTemperature(int temperature) {
        return temperatureTree.floorEntry((byte) (temperature)).getValue();
    }
}
