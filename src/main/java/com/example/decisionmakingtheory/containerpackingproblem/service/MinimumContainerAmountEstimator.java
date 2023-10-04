package com.example.decisionmakingtheory.containerpackingproblem.service;

import com.example.decisionmakingtheory.containerpackingproblem.domain.Cargo;
import com.example.decisionmakingtheory.containerpackingproblem.domain.Result;

import java.util.Arrays;

public interface MinimumContainerAmountEstimator {
    Result calculateMinimum(int carryingCapacityOfContainer, Cargo cargo);

    static int calculateMathematicalMinimum(int carryingCapacityOfContainer, Cargo cargo){
        int sum = Arrays.stream(cargo.goods()).sum();
        return Math.ceilDiv(sum, carryingCapacityOfContainer);
    }
}
