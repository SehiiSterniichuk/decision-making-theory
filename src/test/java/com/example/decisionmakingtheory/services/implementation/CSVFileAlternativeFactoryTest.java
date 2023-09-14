package com.example.decisionmakingtheory.services.implementation;

import com.example.decisionmakingtheory.lab1.config.Config;
import com.example.decisionmakingtheory.lab1.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.lab1.services.AlternativeFactory;
import com.example.decisionmakingtheory.lab1.services.implementation.CSVFileAlternativeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVFileAlternativeFactoryTest {

    private AlternativeFactory factory;

    @BeforeEach
    public void setUp() {
        Config config = new Config();
        config.setPathToCSV("./src/test/resources/fake_input.csv");
        factory = new CSVFileAlternativeFactory(config);
    }

    @Test
    void createTables() {
        List<AlternativeCriteriaTable> tables = factory.createTables();
        assertEquals(tables.size(), 2);
        int[][] table1 = {{1, 1}, {2, 2}};
        int[][] table2 = {{3, 3}, {4, 4}};
        var actual1 = tables.get(0).alternatives();
        var actual2 = tables.get(1).alternatives();
        assertArrayEquals(table1[0], actual1[0]);
        assertArrayEquals(table1[1], actual1[1]);
        assertArrayEquals(table2[0], actual2[0]);
        assertArrayEquals(table2[1], actual2[1]);
    }
}