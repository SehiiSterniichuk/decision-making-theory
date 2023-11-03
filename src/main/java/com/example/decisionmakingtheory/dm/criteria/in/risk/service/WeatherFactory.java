package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.config.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WeatherFactory {
    private final Config config;

    public List<Byte> getTemperatures() {
        List<Byte> temperature;
        try (var reader = new BufferedReader(new FileReader(config.getPathToWeather()))) {
            String line;
            temperature = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                var props = line.split(",");
                temperature.addAll(Arrays.stream(props).map(Byte::parseByte).toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return temperature;
    }
}
