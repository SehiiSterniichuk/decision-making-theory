package com.example.decisionmakingtheory.containerpackingproblem.service.implementation;

import com.example.decisionmakingtheory.containerpackingproblem.domain.Cargo;
import com.example.decisionmakingtheory.containerpackingproblem.domain.OrderType;
import com.example.decisionmakingtheory.containerpackingproblem.domain.Result;
import com.example.decisionmakingtheory.containerpackingproblem.service.MinimumContainerAmountEstimator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Getter
public class FirstFitAlgorithm implements MinimumContainerAmountEstimator {
    private final OrderType order;

    public FirstFitAlgorithm() {
        this.order = OrderType.UNORDERED;
    }

    @Override
    public Result calculateMinimum(int carryingCapacityOfContainer, Cargo cargo) {
        if (cargo.goods().length == 0) {
            return new Result(1, 0);
        }
        List<Integer> containers = new ArrayList<>();
        containers.add(cargo.goods()[0]);
        int complexity = 1;
        int c = 0;//container pointer
        for (int i = 1; i < cargo.goods().length; i++) {
            int good = cargo.goods()[i];
            int currentContainerSize = containers.get(c);
            int newContainerSize = currentContainerSize + good;
            if (newContainerSize <= carryingCapacityOfContainer) {
                containers.set(c, newContainerSize);
                complexity++;
                continue;
            }
            int firstFit = findFit(containers, good, carryingCapacityOfContainer);
            if (firstFit > 0) {
                containers.set(firstFit, containers.get(firstFit) + good);
                complexity += calculateComplexity(containers.size(), firstFit);
                continue;
            }
            containers.add(good);
            complexity += containers.size();
            c++;
        }
        return new Result(complexity, containers.size());
    }

    protected int calculateComplexity(int size, int fit) {
        return switch (order) {
            case DESCENDING, UNORDERED -> size - fit - 1;
            case ASCENDING -> fit + 1;
        };
    }

    protected int findFit(List<Integer> containers, int good, int carryingCapacityOfContainer) {
        int previous = containers.size() - 1;
        return switch (order) {
            case DESCENDING, UNORDERED -> IntStream.range(0, previous)
                    .map(i -> previous - i)
                    .filter(i -> containers.get(i) + good <= carryingCapacityOfContainer)
                    .findFirst().orElse(-1);
            case ASCENDING -> IntStream.range(0, previous)
                    .filter(i -> containers.get(i) + good <= carryingCapacityOfContainer)
                    .findFirst().orElse(-1);
        };
    }

    @Override
    public String getShortName() {
        return "FFA";
    }
}
