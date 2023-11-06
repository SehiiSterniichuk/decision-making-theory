package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RiskService {
    private final AlternativeFactory alternativeFactory;
    private final StrategyEstimator estimator;
    private final ClothesFactory clothesFactory;
    private final WeatherFactory weatherFactory;

    public PriceAlternativeTable getEstimationTable(float coeff) {
        List<Alternative> alternatives = alternativeFactory.getAlternatives();
        List<Clothes> clothes = clothesFactory.getClothes();
        List<Byte> temperatures = weatherFactory.getTemperatures();
        Map<String, Clothes> clothesMap = clothes.stream()
                .collect(Collectors.toMap(Clothes::name, c -> c));
        return estimator.buyNecessaryClothes(clothesMap, alternatives, temperatures, coeff);
    }

    public StrategyResponse getResponse(float coeff) {
        List<Alternative> alternatives = alternativeFactory.getAlternatives();
        List<Clothes> clothes = clothesFactory.getClothes();
        List<Byte> temperatures = weatherFactory.getTemperatures();
        Map<String, Clothes> clothesMap = clothes.stream()
                .collect(Collectors.toMap(Clothes::name, c -> c));
        var table = estimator.buyNecessaryClothes(clothesMap, alternatives, temperatures, coeff);
        StrategyBody body = findStrategy(table, IntStream.range(0, temperatures.size()).map(x -> temperatures.size()).toArray(),
                "Equal probability");
        return new StrategyResponse(List.of(body), table);
    }

    private StrategyBody findStrategy(PriceAlternativeTable table, int[] invertedProbability,
                                      String title) {
        int setsNumber = table.set().size();
        List<Strategy> strategies = new ArrayList<>(setsNumber);
        for (int i = 0; i < setsNumber; i++) {
            ClothesSet set = table.set().get(i);
            float delivering = set.deliveringPrice();
            StringBuilder expression = new StringBuilder();
            float total = 0;
            for (int j = 0; j < set.months().size(); j++) {
                var necessary = set.months().get(j);
                float actualValue = delivering + necessary.value();
                expression
                        .append('(')
                        .append(actualValue)
                        .append('/')
                        .append(invertedProbability[j])
                        .append(')')
                        .append('+');
                total += actualValue / invertedProbability[j];
            }
            expression.deleteCharAt(expression.length() - 1);
            expression.append(')');
            strategies.add(new Strategy(expression.toString(), total));
        }
        int min = findMin(strategies);
        return new StrategyBody(strategies, title, min);
    }

    private int findMin(List<Strategy> strategies) {
        float min = strategies.get(0).value();
        int minIndex = 0;
        for (int i = 1; i < strategies.size(); i++) {
            if (min > strategies.get(i).value()) {
                minIndex = i;
            }
        }
        return minIndex;
    }
}
