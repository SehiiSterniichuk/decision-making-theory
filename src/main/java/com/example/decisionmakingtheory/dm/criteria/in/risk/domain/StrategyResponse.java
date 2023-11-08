package com.example.decisionmakingtheory.dm.criteria.in.risk.domain;

import java.util.List;

public record StrategyResponse(List<StrategyBody> strategyBodies,
                               PriceAlternativeTable table, PriceAlternativeTable discountTable,
                               List<WeatherData> weatherData) {
}