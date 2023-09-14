package com.example.decisionmakingtheory.services.implementation;

import com.example.decisionmakingtheory.lab1.config.Config;
import com.example.decisionmakingtheory.lab1.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.lab1.domain.Domination;
import com.example.decisionmakingtheory.lab1.services.Algorithm;
import com.example.decisionmakingtheory.lab1.services.implementation.CSVFileAlternativeFactory;
import com.example.decisionmakingtheory.lab1.services.implementation.ParetoAlgorithm;
import com.example.decisionmakingtheory.lab1.services.implementation.SlaterAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AbstractAlgorithmTest {

    private final AlternativeCriteriaTable table;
    private final Algorithm pareto;
    private final Algorithm slater;

    AbstractAlgorithmTest() {
        Config config = new Config();
        config.setPathToCSV("./src/test/resources/example.csv");
        table = (new CSVFileAlternativeFactory(config)).createTables().get(0);
        slater = new SlaterAlgorithm();
        pareto = new ParetoAlgorithm();
    }

    @Test
    void applyPareto() {
        int[] expected = {-1, -1, 0, 0, 1, -1, 0, 0, 1, 0};
        checkAlgo(pareto, expected);
    }
    @Test
    void applySlater() {
        int[] expected = {-1, -1, 0, 0, -1, -1, 0, 0, 1, 0};
        checkAlgo(slater, expected);
    }

    private void checkAlgo(Algorithm algorithm, int[] expected) {
        Domination domination = algorithm.apply(table);
        int[] actual = domination.a();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            if(expected[i] == -1){
                if(expected[i] != actual[i]){
                    log.error("i: " + i + " actual[i]: " + actual[i]);
                    throw new RuntimeException();
                }
            }else if(actual[i] == -1){
                log.error("i: " + i + " actual[i]: " + actual[i]);
                throw new RuntimeException();
            }
        }
    }
}