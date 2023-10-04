package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service;

import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.Domination;

import java.util.function.Function;

public interface Algorithm extends Function<AlternativeCriteriaTable, Domination> {
    String getName();
}
