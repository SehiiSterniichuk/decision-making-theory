package com.example.decisionmakingtheory.lab1.services;

import com.example.decisionmakingtheory.lab1.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.lab1.domain.Domination;

import java.util.function.Function;

public interface Algorithm extends Function<AlternativeCriteriaTable, Domination> {
    String getName();
}
