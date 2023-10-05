package com.example.decisionmakingtheory.containerpackingproblem.service.implementation;

import com.example.decisionmakingtheory.containerpackingproblem.domain.*;
import com.example.decisionmakingtheory.containerpackingproblem.service.MinimumContainerAmountEstimator;
import com.example.decisionmakingtheory.containerpackingproblem.service.PackingProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackingProblemServiceImpl implements PackingProblemService {
    private final CSVCargoFactory factory;

    @Override
    public ExplorationStatistics makeDefaultExploration(int capacity) {
        List<Cargo> unorderedCargos = new ArrayList<>(factory.getCargos());
        Cargo all = Cargo.combineAll(unorderedCargos);
        unorderedCargos.add(all);
        List<Integer> mathResult = unorderedCargos.stream()
                .map(i -> MinimumContainerAmountEstimator.calculateMathematicalMinimum(capacity, i))
                .toList();
        List<Cargo> orderedAscendingCargos = unorderedCargos
                .stream()
                .map(i -> new Cargo(Arrays.stream(i.goods()).sorted().toArray()))
                .toList();
        List<Cargo> orderedDescendingCargos = unorderedCargos
                .stream()
                .map(i -> new Cargo(Arrays.stream(i.goods()).boxed().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue).toArray()))
                .toList();
        AlgorithmResults unordered = doAlgorithm(unorderedCargos, "Unordered", MinimumContainerAmountEstimator.getAll(OrderType.UNORDERED), capacity);
        AlgorithmResults orderedAscending = doAlgorithm(orderedAscendingCargos, "Ordered Ascending", MinimumContainerAmountEstimator.getAll(OrderType.ASCENDING), capacity);
        AlgorithmResults orderedDescending = doAlgorithm(orderedDescendingCargos, "Ordered Descending", MinimumContainerAmountEstimator.getAll(OrderType.DESCENDING), capacity);
        return ExplorationStatistics.builder()
                .mathCalculation(mathResult)
                .algorithmData(List.of(unordered, orderedAscending, orderedDescending))
                .build();
    }

    private AlgorithmResults doAlgorithm(List<Cargo> cargos, String title,
                                         List<MinimumContainerAmountEstimator> algo, int capacity) {
        int[][] counter = new int[cargos.size()][algo.size()];
        int[][] complexity = new int[cargos.size()][algo.size()];
        String[] algoNames = algo.stream().map(MinimumContainerAmountEstimator::getShortName).toArray(String[]::new);
        for (int i = 0; i < cargos.size(); i++) {
            var cargo = cargos.get(i);
            for (int j = 0; j < algo.size(); j++) {
                var estimator = algo.get(j);
                Result r = estimator.calculateMinimum(capacity, cargo);
                counter[i][j] = r.minimumAmountOfContainers();
                complexity[i][j] = r.complexity();
            }
        }
        return new AlgorithmResults(title, algoNames, counter, complexity);
    }
}
