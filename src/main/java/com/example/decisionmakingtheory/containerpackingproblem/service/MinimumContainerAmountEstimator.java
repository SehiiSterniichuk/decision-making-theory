package com.example.decisionmakingtheory.containerpackingproblem.service;

import com.example.decisionmakingtheory.containerpackingproblem.domain.Cargo;
import com.example.decisionmakingtheory.containerpackingproblem.domain.OrderType;
import com.example.decisionmakingtheory.containerpackingproblem.domain.Result;
import com.example.decisionmakingtheory.containerpackingproblem.service.implementation.BestFitAlgorithm;
import com.example.decisionmakingtheory.containerpackingproblem.service.implementation.FirstFitAlgorithm;
import com.example.decisionmakingtheory.containerpackingproblem.service.implementation.NextFitAlgorithm;
import com.example.decisionmakingtheory.containerpackingproblem.service.implementation.WorstFitAlgorithm;

import java.util.Arrays;
import java.util.List;

public interface MinimumContainerAmountEstimator {
    static List<MinimumContainerAmountEstimator> getAll(OrderType order) {
        return List.of(new NextFitAlgorithm(),
                new FirstFitAlgorithm(order),
                new WorstFitAlgorithm(order),
                new BestFitAlgorithm(order));
    }

    String getShortName();

    Result calculateMinimum(int carryingCapacityOfContainer, Cargo cargo);

    static int calculateMathematicalMinimum(int carryingCapacityOfContainer, Cargo cargo) {
        int sum = Arrays.stream(cargo.goods()).sum();
        return Math.ceilDiv(sum, carryingCapacityOfContainer);
    }
}
