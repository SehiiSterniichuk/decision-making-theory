package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service;

import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.AlternativeCriteriaTable;

import java.util.List;

public interface AlternativeFactory {
    List<AlternativeCriteriaTable> createTables();
}
