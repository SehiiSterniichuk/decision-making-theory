package com.example.decisionmakingtheory.dm.criteria.in.risk.domain;

import java.util.List;

public record StrategyBody(List<Strategy> strategies, String title, int bestIndex) {
}
