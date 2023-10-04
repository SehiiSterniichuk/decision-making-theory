package com.example.decisionmakingtheory.containerpackingproblem.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record ExplorationStatistics(
        List<Integer> mathCalculation,
        List<AlgorithmResults> algorithmData
) {
}
