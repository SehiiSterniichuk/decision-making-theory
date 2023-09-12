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
        try (LineNumberReader reader = new LineNumberReader(new FileReader(config.getPathToCSV()))){
            String line;
            list = new ArrayList<>(3);
            while ((line = reader.readLine()) != null){
                String[] split = line.split(",");
                int[] q1 = new int[split.length];
                int[] q2 = new int[split.length];
                for (int i = 0; i < split.length; i++) {
                    var value = split[i];
                    int a1 = value.charAt(0) - '0';
                    int a2 = value.charAt(1) - '0';
                    q1[i] = a1;
                    q2[i] = a2;
                }
                AlternativeCriteriaTable table = new AlternativeCriteriaTable(new int[][]{q1, q2});
                list.add(table);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return List.of();
        }
        return list;
    }
}
