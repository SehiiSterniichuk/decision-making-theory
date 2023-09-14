package com.example.decisionmakingtheory.lab1.services;

import com.example.decisionmakingtheory.lab1.domain.AlternativeCriteriaTable;

import java.util.List;

public interface AlternativeFactory {
    List<AlternativeCriteriaTable> createTables();
}
