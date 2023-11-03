package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.Alternative;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AlternativeFactory {
    private final Config config;

    public List<Alternative> getAlternatives() {
        List<Alternative> alternatives;
        try (var reader = new BufferedReader(new FileReader(config.getPathToClothesAlternatives()))) {
            String line;
            alternatives = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                var props = line.replace("-","").split(",");
                if (props.length != 8) {
                    throw new IllegalArgumentException("Alternative should have 8 parameters");
                }
                var temp = getTemperature(props);
                var alternative = new Alternative(temp.min(), temp.max(),
                        props[2], props[3],
                        props[4], props[5],
                        props[6], Float.parseFloat(props[7]));
                alternatives.add(alternative);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return alternatives;
    }

    record Temperature(byte min, byte max) {
    }

    private Temperature getTemperature(String[] props) {
        if (props[0].isBlank()) {
            return new Temperature(Byte.MIN_VALUE, Byte.parseByte(props[1]));
        } else if (props[1].isBlank()) {
            return new Temperature(Byte.parseByte(props[0]), Byte.MAX_VALUE);
        }
        return new Temperature(Byte.parseByte(props[0]), Byte.parseByte(props[1]));
    }
}
