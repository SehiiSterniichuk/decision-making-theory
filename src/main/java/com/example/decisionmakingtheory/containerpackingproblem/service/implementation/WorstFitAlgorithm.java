package com.example.decisionmakingtheory.containerpackingproblem.service.implementation;

import com.example.decisionmakingtheory.containerpackingproblem.domain.OrderType;

import java.util.List;

public class WorstFitAlgorithm extends FirstFitAlgorithm {

    public WorstFitAlgorithm(OrderType order) {
        super(order);
    }

    @Override
    protected int findFit(List<Integer> containers, int good, int carryingCapacityOfContainer) {
        if (getOrder() != OrderType.UNORDERED){
            return super.findFit(containers, good, carryingCapacityOfContainer);
        }
        int previous = containers.size() - 1;
        int mostEmptyIndex = -1;
        int mostEmptySize = -1;
        for (int i = 0; i < previous; i++) {
            int curSize = containers.get(i);
            if (curSize + good <= carryingCapacityOfContainer && curSize < mostEmptySize) {
                mostEmptyIndex = i;
                mostEmptySize = curSize;
            }
        }
        return mostEmptyIndex;
    }



    @Override
    protected int calculateComplexity(int size, int fit) {
        return switch (getOrder()) {
            case DESCENDING -> size - fit - 1;
            case UNORDERED -> size - 1;
            case ASCENDING -> fit + 1;
        };
    }

    @Override
    public String getShortName(){
        return "WFA";
    }
}
