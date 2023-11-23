package com.example.decisionmakingtheory.votingmethods.domain;

import java.util.Arrays;

public record ElectionData(int[] votes, char[][] rating) {
    @Override
    public String toString() {
        return "ElectionData{" +
                "votes=" + Arrays.toString(votes) +
                ", rating=" + Arrays.toString(Arrays.stream(rating).map(Arrays::toString).toArray()) +
                '}';
    }
}
