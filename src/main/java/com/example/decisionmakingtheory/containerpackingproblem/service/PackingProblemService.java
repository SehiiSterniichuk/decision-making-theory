package com.example.decisionmakingtheory.containerpackingproblem.service;

import com.example.decisionmakingtheory.containerpackingproblem.domain.ExplorationStatistics;

public interface PackingProblemService {

    ExplorationStatistics makeDefaultExploration(int capacity);
}
