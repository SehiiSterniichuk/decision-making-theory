package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.Clothes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClothesFactory {
    private final Config config;

    public List<Clothes> getClothes() {
        List<Clothes> list;
        try (var reader = new BufferedReader(new FileReader(config.getPathToClothes()))) {
            list = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                var data = line.split(",");
                if (data.length != 3) {
                    throw new IllegalArgumentException("Clothes should have three fields");
                }
                list.add(new Clothes(data[0], Float.parseFloat(data[1]), Float.parseFloat(data[2])));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
