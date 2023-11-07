package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    record Variant(String title, int[] invertedProbability) {
    }

    private final Variant equalYearProbability = new Variant("Equal probability",
            IntStream.range(0, 12).map(x -> 12).toArray());
    private final Variant winter = new Variant("Winter", IntStream.range(0, 12)
            .map(x -> Set.of(0, 1, 11).contains(x) ? 3 : Integer.MIN_VALUE).toArray());
    private final Variant spring = new Variant("Spring", IntStream.range(0, 12)
            .map(x -> Set.of(2, 3, 4).contains(x) ? 3 : Integer.MIN_VALUE).toArray());

    private final Variant summer = new Variant("Summer", IntStream.range(0, 12)
            .map(x -> Set.of(5, 6, 7).contains(x) ? 3 : Integer.MIN_VALUE).toArray());

    private final Variant autumn = new Variant("Autumn", IntStream.range(0, 12)
            .map(x -> Set.of(8, 9, 10).contains(x) ? 3 : Integer.MIN_VALUE).toArray());

    private final Variant winter3 = new Variant("Winter x3", IntStream.range(0, 12)
            .map(x -> Set.of(0, 1, 11).contains(x) ? 6 : 18).toArray());

    List<Variant> variants = List.of(equalYearProbability, winter, spring, summer, autumn, winter3);

    public StrategyResponse getResponse(float coeff) {
        List<Alternative> alternatives = alternativeFactory.getAlternatives();
        List<Clothes> clothes = clothesFactory.getClothes();
        List<Byte> temperatures = weatherFactory.getTemperatures();
        assert temperatures.size() == 12;
        Map<String, Clothes> clothesMap = clothes.stream()
                .collect(Collectors.toMap(Clothes::name, c -> c));
        var table = estimator.buyNecessaryClothes(clothesMap, alternatives, temperatures, coeff);
        List<StrategyBody> strategyBodies = new ArrayList<>(variants.size() + 1);
        variants.forEach(x -> strategyBodies.add(findStrategy(table, x.invertedProbability(), x.title())));
        return new StrategyResponse(strategyBodies, table);
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
                if (invertedProbability[j] == Integer.MIN_VALUE) {
                    continue;
                }
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
                min = strategies.get(i).value();
            }
        }
        return minIndex;
    }
}
