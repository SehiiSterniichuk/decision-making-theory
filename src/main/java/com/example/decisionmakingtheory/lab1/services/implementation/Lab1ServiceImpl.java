package com.example.decisionmakingtheory.lab1.services.implementation;

import com.example.decisionmakingtheory.lab1.domain.*;
import com.example.decisionmakingtheory.lab1.services.Lab1Service;
import com.example.decisionmakingtheory.lab1.services.Algorithm;
import com.example.decisionmakingtheory.lab1.services.AlternativeFactory;
import com.example.decisionmakingtheory.lab1.services.ResultProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class Lab1ServiceImpl implements Lab1Service {
    private final AlternativeFactory factory;
    private final ResultProcessor resultProcessor;
    private final Algorithm pareto = new ParetoAlgorithm();
    private final Algorithm slater = new SlaterAlgorithm();

    @Override
    public List<LabResult> results() {
        List<AlternativeCriteriaTable> createTables = factory.createTables();
        int size = createTables.size();
        List<LabResult> results = new ArrayList<>(size + 1);
        for (int i = 0; i < size; i++) {
            var table = createTables.get(i);
            LabResult labResult = getLabResult(table, i);
            results.add(labResult);
        }
        results.add(getLabResult(combineTablesIntoOne(size, createTables), size));
        return results;
    }

    private LabResult getLabResult(AlternativeCriteriaTable table, int i) {
        MethodResult paretoRes = getMethodResult(pareto, i, table);
        MethodResult slaterRes = getMethodResult(slater, i, table);
        return LabResult.builder().table(table).methods(List.of(paretoRes, slaterRes)).build();
    }

    private MethodResult getMethodResult(Algorithm algo, int i, AlternativeCriteriaTable table) {
        var time = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).toString().replace(":", "_")
                .replace(".", "_");
        String resultName = algo.getName() + i;
        var fileName = "scatter_" + resultName + "_" + time + ".png";
        Domination domination = algo.apply(table);
        resultProcessor.processResult(table, domination, resultName, fileName);
        List<String> dominationView = convertDominationToString(domination);
        return MethodResult.builder()
                .methodName(algo.getName())
                .fileName(fileName)
                .domination(new DominationView(dominationView))
                .build();
    }

    private static List<String> convertDominationToString(Domination domination) {
        int length = domination.a().length;
        List<String> dominationView = new ArrayList<>(length);
        for (int j = 0; j < length; j++) {
            int a = domination.a()[j];
            String stringRepresentation = a != -1 ? "A" + (j + 1) : "";
            dominationView.add(stringRepresentation);
        }
        return dominationView;
    }

    private static AlternativeCriteriaTable combineTablesIntoOne(int size, List<AlternativeCriteriaTable> createTables) {
        int[][] all = new int[size * (createTables.get(0).alternatives().length)][];
        for (int i = 0, j = 0; i < size; i++) {
            var tableData = createTables.get(i).alternatives();
            for (; j < tableData.length * (i + 1); j++) {
                all[j] = tableData[j % tableData.length];
            }
        }
        return new AlternativeCriteriaTable(all);
    }




}
