package com.example.decisionmakingtheory.services.implementation;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.services.AlternativeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CSVFileAlternativeFactory implements AlternativeFactory {
    private final Config config;

    @Override
    public List<AlternativeCriteriaTable> createTables() {
        List<AlternativeCriteriaTable> list;
        try (LineNumberReader reader = new LineNumberReader(new FileReader(config.getPathToCSV()))) {
            String line;
            list = new ArrayList<>(3);
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                int[][] data = new int[split.length][2];
                for (int i = 0; i < split.length; i++) {
                    var value = split[i];
                    if (value.length() == 1) {
                        data[i] = new int[]{0, value.charAt(0) - '0'};
                        continue;
                    }
                    int a1 = value.charAt(0) - '0';
                    int a2 = value.charAt(1) - '0';
                    data[i] = new int[]{a1, a2};
                }
                AlternativeCriteriaTable table = new AlternativeCriteriaTable(data);
                list.add(table);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return List.of();
        }
        return list;
    }
}
