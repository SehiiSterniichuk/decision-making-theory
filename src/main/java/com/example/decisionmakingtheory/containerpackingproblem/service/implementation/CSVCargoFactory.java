package com.example.decisionmakingtheory.containerpackingproblem.service.implementation;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.containerpackingproblem.domain.Cargo;
import com.example.decisionmakingtheory.containerpackingproblem.service.CargoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CSVCargoFactory implements CargoFactory {
    private final Config config;

    @Override
    public List<Cargo> getCargos() {
        File source = new File(config.getPathToCSV());
        if (!source.exists() || !source.isFile()) {
            throw new RuntimeException("file doesn't exist: " + source.getPath());
        }
        List<Cargo> cargos = new ArrayList<>(3);
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] array = line.split(",");
                int[] goods = Arrays.stream(array)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                cargos.add(new Cargo(goods));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cargos;
    }
}
