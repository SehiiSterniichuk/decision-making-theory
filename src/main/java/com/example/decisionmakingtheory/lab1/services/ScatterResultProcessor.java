package com.example.decisionmakingtheory.lab1.services;

import com.example.decisionmakingtheory.lab1.config.Config;
import com.example.decisionmakingtheory.lab1.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.lab1.domain.Domination;
import com.example.decisionmakingtheory.lab1.domain.PlotData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ScatterResultProcessor implements ResultProcessor {
    private final PNGScatterPainter painter;
    private final Config config;
    @Override
    public void processResult(AlternativeCriteriaTable table, Domination domination, String resultName, String fileName) {
        PlotData build = PlotData.builder()
                .height(600)
                .width(600)
                .xAxis("Q1")
                .yAxis("Q2")
                .title(resultName)
                .series(resultName)
                .build();
        String path = config.getPathToResultFolder() + File.separator
                + fileName;
        File img;
        try {
            img = Files.createFile(Path.of(path)).toFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        painter.drawScatter(img, table, domination, build);
    }
}
