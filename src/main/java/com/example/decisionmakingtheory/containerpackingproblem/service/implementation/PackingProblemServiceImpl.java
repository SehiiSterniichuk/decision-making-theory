package com.example.decisionmakingtheory.containerpackingproblem.service.implementation;

import com.example.decisionmakingtheory.containerpackingproblem.domain.AlgorithmResults;
import com.example.decisionmakingtheory.containerpackingproblem.domain.Cargo;
import com.example.decisionmakingtheory.containerpackingproblem.domain.ExplorationStatistics;
import com.example.decisionmakingtheory.containerpackingproblem.service.MinimumContainerAmountEstimator;
import com.example.decisionmakingtheory.containerpackingproblem.service.PackingProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackingProblemServiceImpl implements PackingProblemService {
    private final CSVCargoFactory factory;

    @Override
    public ExplorationStatistics makeDefaultExploration() {
        List<Cargo> cargos = new ArrayList<>(factory.getCargos());
        Cargo all = Cargo.combineAll(cargos);
        cargos.add(all);
        List<Integer> mathResult = cargos.stream()
                .map(i -> MinimumContainerAmountEstimator.calculateMathematicalMinimum(100, i))
                .toList();
        List<AlgorithmResults> algorithmResults = new ArrayList<>(cargos.size());
        return ExplorationStatistics.builder()
                .mathCalculation(mathResult)
                .build();
    }

    private AlgorithmResults doWithoutOrdering(Cargo i) {
        return null;
    }
}
