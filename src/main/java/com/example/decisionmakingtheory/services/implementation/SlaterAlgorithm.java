package com.example.decisionmakingtheory.services.implementation;

public class SlaterAlgorithm extends AbstractAlgorithm {
    @Override
    protected boolean checkCondition(int[] x1, int[] x2) {
        for (int i = 0; i < x1.length; i++) {
            if (x2[i] >= x1[i]) {
                return false;
            }
        }
        return true;
    }
}
