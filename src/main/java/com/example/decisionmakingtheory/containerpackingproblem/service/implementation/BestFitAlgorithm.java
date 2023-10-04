package com.example.decisionmakingtheory.containerpackingproblem.service.implementation;

import java.util.List;

public class BestFitAlgorithm extends FirstFitAlgorithm {
    @Override
    protected int findFirstFit(List<Integer> containers, int good, int carryingCapacityOfContainer) {
        int previous = containers.size() - 1;
        int fullestIndex = -1;
        int fullestSize = -1;
        for (int i = 0; i < previous; i++) {
            int curSize = containers.get(i);
            if (curSize + good <= carryingCapacityOfContainer && curSize > fullestSize) {
                fullestIndex = i;
                fullestSize = curSize;
            }
        }
        return fullestIndex;
    }
}
