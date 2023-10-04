package com.example.decisionmakingtheory.containerpackingproblem.service.implementation;

import com.example.decisionmakingtheory.containerpackingproblem.domain.Cargo;
import com.example.decisionmakingtheory.containerpackingproblem.domain.Result;
import com.example.decisionmakingtheory.containerpackingproblem.service.MinimumContainerAmountEstimator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ContainerAmountEstimatorTest {
    int[] goods = {23, 3, 51, 20, 51, 42, 52, 12, 4, 7, 59, 58, 25, 94, 18};
    Cargo example = new Cargo(goods);

    @Test
    void nextFitAlgorithm() {
        NextFitAlgorithm a = new NextFitAlgorithm();
        Result actual = a.calculateMinimum(100, example);
        Result expected = new Result(15, 7);
        assertEquals(expected, actual);
    }

    @Test
    void firstFitAlgorithm() {
        MinimumContainerAmountEstimator a = new FirstFitAlgorithm();
        Result actual = a.calculateMinimum(100, example);
        System.out.println(actual);
        assertEquals(6, actual.minimumAmountOfContainers());
    }

    @Test
    void bestFitAlgorithm() {
        MinimumContainerAmountEstimator a = new BestFitAlgorithm();
        Result actual = a.calculateMinimum(100, example);
        System.out.println(actual);
        assertEquals(6, actual.minimumAmountOfContainers());
    }
}