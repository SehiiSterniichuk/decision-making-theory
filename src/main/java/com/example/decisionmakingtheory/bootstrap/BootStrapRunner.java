package com.example.decisionmakingtheory.bootstrap;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.domain.Domination;
import com.example.decisionmakingtheory.services.Algorithm;
import com.example.decisionmakingtheory.services.AlternativeFactory;
import com.example.decisionmakingtheory.services.ResultProcessor;
import com.example.decisionmakingtheory.services.implementation.SlaterAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootStrapRunner implements CommandLineRunner {
    private final Config config;
    private final AlternativeFactory factory;
    private final ResultProcessor resultProcessor;

    @Override
    public void run(String... args) throws IOException {
        log.info(config.getPathToCSV());
        var pathToResultFolder = Path.of(config.getPathToResultFolder());
        deleteDirectory(pathToResultFolder);
        Files.createDirectory(Path.of(config.getPathToResultFolder()));
        Algorithm algo = new SlaterAlgorithm();
        List<AlternativeCriteriaTable> createTables = factory.createTables();
        int size = createTables.size();
        for (int i = 0; i < size; i++) {
            var table = createTables.get(i);
            monkeyJob(table, i, algo);
        }
        int[][] all = new int[size * (createTables.get(0).alternatives().length)][];
        for (int i = 0, j = 0; i < size; i++) {
            var tableData = createTables.get(i).alternatives();
            for (; j < tableData.length * (i + 1); j++) {
                all[j] = tableData[j % tableData.length];
            }
        }
        var table = new AlternativeCriteriaTable(all);
        System.out.println(table);
        monkeyJob(table, 10, algo);
    }

    private void monkeyJob(AlternativeCriteriaTable table, int i, Algorithm algo) {
        Domination domination = algo.apply(table);
        resultProcessor.processResult(table, domination, algo.getName() + i);
    }

    public static void deleteDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            try (Stream<Path> walk = Files.walk(directory)) {
                walk.sorted((a, b) -> -a.compareTo(b)) // Reverse order for files
                        .forEach(file -> {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        });
            }
        } else {
            log.info("Directory does not exist: " + directory);
        }
    }
}
