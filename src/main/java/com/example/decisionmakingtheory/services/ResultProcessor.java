package com.example.decisionmakingtheory.services;

import com.example.decisionmakingtheory.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.domain.Domination;

public interface ResultProcessor {
    void processResult(AlternativeCriteriaTable table, Domination domination, String resultName);
}
