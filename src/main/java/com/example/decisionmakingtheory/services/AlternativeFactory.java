package com.example.decisionmakingtheory.services;

import com.example.decisionmakingtheory.domain.AlternativeCriteriaTable;

import java.util.List;

public interface AlternativeFactory {
    List<AlternativeCriteriaTable> createTables();
}
