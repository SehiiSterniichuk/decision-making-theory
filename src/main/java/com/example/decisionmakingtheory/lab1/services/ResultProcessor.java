package com.example.decisionmakingtheory.lab1.services;

import com.example.decisionmakingtheory.lab1.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.lab1.domain.Domination;

public interface ResultProcessor {
    void processResult(AlternativeCriteriaTable table, Domination domination, String resultName, String fileName);
}
