package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service;

import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.Domination;

public interface ResultProcessor {
    void processResult(AlternativeCriteriaTable table, Domination domination, String resultName, String fileName);
}
