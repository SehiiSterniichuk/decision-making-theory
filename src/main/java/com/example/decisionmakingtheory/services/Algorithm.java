package com.example.decisionmakingtheory.services;

import com.example.decisionmakingtheory.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.domain.Domination;

import java.util.function.Function;

public interface Algorithm extends Function<AlternativeCriteriaTable, Domination> {
}
