package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.implementation;

import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.*;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.Algorithm;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.AlternativeFactory;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.Lab1Service;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.ResultProcessor;
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

    @Override
    public List<LabResult> testResults(double z1x1, double z1x2, double z2x1, double z2x2, double r, double R) {
        List<AlternativeCriteriaTable> createTables = factory.createTables();
        int size = createTables.size();
        List<LabResult> results = new ArrayList<>(size + 1);
        for (int i = 0; i < size; i++) {
            var table = createTables.get(i);
            LabResult labResult = getLabResult(table, i);
            results.add(labResult);
        }
        results.add(getLabResult(createTableWith4Coeff(createTables.get(0), z1x1, z1x2, z2x1, z2x2), 1));
        results.add(getLabResult(createTableWith2Coeff(createTables.get(0), r, R), 2));
        return results;
    }

    private AlternativeCriteriaTable createTableWith2Coeff(AlternativeCriteriaTable alternativeCriteriaTable, double r, double R) {
        List<List<Integer>> newData = new ArrayList<>();
        var oldData = alternativeCriteriaTable.alternatives();
        for (int[] alternative : oldData) {
            int x1 = alternative[0];
            int x2 = alternative[1];
            boolean c1 = -x1 + x2 <= R;
            boolean c2 = -x1 + x2 >= -R;
            boolean c3 = x1 + x2 >= -R;
            if (!(c1 && c2 && c3)) {
                continue;
            }
            boolean c21 = x1 + x2 >= -R;
            boolean c22 = -R + r <= x1 && x1 <= R - r;
            boolean c23 = -R + r <= x2 && x2 <= R - r;
            if (!(c21 && c22 && c23)) {
                continue;
            }
            var z1 = (int) Math.ceil(x1 * r);
            var z2 = (int) Math.ceil(x2 * R);
            newData.add(List.of(z1, z2));
        }
        int[][] newDataAsArray = newData.stream().map(l -> new int[]{l.get(0), l.get(1)}).toArray(int[][]::new);
        return new AlternativeCriteriaTable(newDataAsArray);
    }

    private AlternativeCriteriaTable createTableWith4Coeff(AlternativeCriteriaTable alternativeCriteriaTable,
                                                           double z1x1, double z1x2, double z2x1, double z2x2) {
        int[][] newData = new int[alternativeCriteriaTable.getLength()][alternativeCriteriaTable.alternatives()[0].length];
        var oldData = alternativeCriteriaTable.alternatives();
        for (int i = 0; i < oldData.length; i++) {
            int[] alternative = oldData[i];
            int z1 = (int) Math.ceil(z1x1 * alternative[0] + z1x2 * alternative[1]);
            int z2 = (int) Math.ceil(z2x1 * alternative[0] + z2x2 * alternative[1]);
            newData[i][0] = z1;
            newData[i][1] = z2;
        }
        return new AlternativeCriteriaTable(newData);
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
