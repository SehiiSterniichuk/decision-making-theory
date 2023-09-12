package com.example.decisionmakingtheory.services;

import com.example.decisionmakingtheory.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.domain.Decision;

import java.util.function.Function;

@SuppressWarnings("unused")
public interface Algorithm extends Function<AlternativeCriteriaTable, Decision> {
}
