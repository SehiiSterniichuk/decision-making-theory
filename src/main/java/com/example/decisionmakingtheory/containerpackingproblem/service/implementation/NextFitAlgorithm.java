package com.example.decisionmakingtheory.containerpackingproblem.service.implementation;

import com.example.decisionmakingtheory.containerpackingproblem.domain.Cargo;
import com.example.decisionmakingtheory.containerpackingproblem.domain.Result;
import com.example.decisionmakingtheory.containerpackingproblem.service.MinimumContainerAmountEstimator;

import java.util.ArrayList;
import java.util.List;

public class NextFitAlgorithm implements MinimumContainerAmountEstimator {
    @Override
    public Result calculateMinimum(int carryingCapacityOfContainer, Cargo cargo) {
        if (cargo.goods().length == 0) {
            return new Result(1, 0);
        }
        List<Integer> containers = new ArrayList<>();
        containers.add(cargo.goods()[0]);
        int complexity = 1;
        int container = 0;
        for (int i = 1; i < cargo.goods().length; i++) {
            int good = cargo.goods()[i];
            int currentContainerSize = containers.get(container);
            int newContainerSize = currentContainerSize + good;
            if (newContainerSize <= carryingCapacityOfContainer) {
                containers.set(container, newContainerSize);
            } else {
                containers.add(good);
                container++;
            }
            complexity++;
        }
        return new Result(complexity, containers.size());
    }

    @Override
    public String getShortName(){
        return "NFA";
    }
}
