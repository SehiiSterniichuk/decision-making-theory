package com.example.decisionmakingtheory.services.implementation;

import com.example.decisionmakingtheory.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.domain.Domination;
import com.example.decisionmakingtheory.services.Algorithm;

import java.util.Arrays;

public abstract class AbstractAlgorithm implements Algorithm {
    @Override
    public final Domination apply(AlternativeCriteriaTable alternativeCriteriaTable) {
        int[][] alternatives = alternativeCriteriaTable.alternatives();
        for (int i = 1; i < alternatives.length; i++) {
            assert alternatives[i - 1].length == alternatives[i].length;
        }
        int[] domination = new int[alternatives.length];
        Arrays.fill(domination, -1);
        final int N = alternatives.length;
        for (int i = 0, j = 1; ; ) {
            if (domination[j] == -1 && domination[i] == -1 && !Arrays.equals(alternatives[j], alternatives[i])) {
                if (checkCondition(alternatives[i], alternatives[j])) {
                    domination[j] = i;
                } else if (checkCondition(alternatives[j], alternatives[i])) {
                    domination[i] = j;
                }
            }
            if (j < N - 1) {
                j++;
            } else if (i < N - 2) {
                i++;
                j = i + 1;
            } else {
                break;
            }
        }
        return new Domination(domination);
    }

    protected abstract boolean checkCondition(int[] x1, int[] x2);
}
