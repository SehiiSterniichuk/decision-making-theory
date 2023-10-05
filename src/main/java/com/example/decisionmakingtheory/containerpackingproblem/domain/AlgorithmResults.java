package com.example.decisionmakingtheory.containerpackingproblem.domain;

public record AlgorithmResults(
        String tableTitle,

        String[] algorithmName,
        int[][] containerCounter,
        int[][] complexity
) {
}
