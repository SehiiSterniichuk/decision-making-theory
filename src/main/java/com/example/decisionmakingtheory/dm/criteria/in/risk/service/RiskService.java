package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class RiskService {
    private final AlternativeFactory alternativeFactory;
    private final StrategyEstimator estimator;
    private final ClothesFactory clothesFactory;
    private final WeatherFactory weatherFactory;

    record Variant(String title, int[] invertedProbability) {
    }

    record VariantWithCoeff(String title, int[] invertedProbability, int[] probabilityCoeff) {
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
    Set<Integer> discountClothes = Set.of(1, 3, 8, 14, 15);

    public StrategyResponse getResponse(float coeff, float priceDivisor) {
        List<Alternative> alternatives = alternativeFactory.getAlternatives();
        List<Clothes> clothes = clothesFactory.getClothes();
        List<Byte> temperatures = weatherFactory.getTemperatures();
        assert temperatures.size() == 12;
        Map<String, Clothes> clothesMap = clothes.stream().collect(Collectors.toMap(Clothes::name, c -> c));
        var table = estimator.buyNecessaryClothes(clothesMap, alternatives, temperatures, coeff);
        List<StrategyBody> strategyBodies = new ArrayList<>(variants.size() + 2);
        // calculate with defined probabilities
        variants.forEach(x -> strategyBodies.add(findStrategy(table, x.invertedProbability(), x.title())));
        // calculate depending on days in a month
        var daysInMonth = getInvertedProbabilityDependingOnDaysInMonth();
        strategyBodies.add(findStrategy(table, daysInMonth.invertedProbability(), daysInMonth.probabilityCoeff(), daysInMonth.title()));
        //calculate with discount
        clothesMap = getNewClothesMap(priceDivisor, clothes);
        PriceAlternativeTable discountTable = estimator.buyNecessaryClothes(clothesMap, alternatives, temperatures, coeff);
        strategyBodies.add(findStrategy(discountTable,
                equalYearProbability.invertedProbability(), equalYearProbability.title + " with discount"));
        List<WeatherData> weatherData = getWeatherData(temperatures);
        return new StrategyResponse(strategyBodies, table,discountTable, weatherData);
    }

    private Map<String, Clothes> getNewClothesMap(float priceDivisor, List<Clothes> clothes) {
        List<Clothes> newClothes = IntStream.range(0, clothes.size())
                .mapToObj(i -> discountClothes.contains(i) ?
                        clothes.get(i).withNewPrice(p -> p / priceDivisor)
                        : clothes.get(i)).toList();
        return newClothes.stream().collect(Collectors.toMap(Clothes::name, c -> c));
    }

    private static List<WeatherData> getWeatherData(List<Byte> temperatures) {
        return IntStream.range(1, 13).mapToObj(i -> new WeatherData(getMonth(i), temperatures.get(i - 1))).toList();
    }

    private static String getMonth(int i) {
        String upper = Month.of(i).toString();
        return upper.charAt(0) + upper.toLowerCase().substring(1, upper.length());
    }


    private VariantWithCoeff getInvertedProbabilityDependingOnDaysInMonth() {
        int currentYear = YearMonth.now().getYear();
        int[] daysInMonth = new int[12];
        int lengthOfTheYear = Year.now().length();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            daysInMonth[month - 1] = yearMonth.lengthOfMonth();
        }
        return new VariantWithCoeff("Depending on days in a month of " + currentYear,
                IntStream.range(0, 12).map(x -> lengthOfTheYear).toArray(), daysInMonth);
    }

    private StrategyBody findStrategy(PriceAlternativeTable table, int[] invertedProbability,
                                      String title) {
        return findStrategy(table, invertedProbability, null, title);
    }

    private StrategyBody findStrategy(PriceAlternativeTable table, int[] invertedProbability,
                                      int[] probabilityCoeff,
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
                if (probabilityCoeff != null) {
                    expression
                            .append(probabilityCoeff[j])
                            .append('*');
                    total += (actualValue / invertedProbability[j]) * probabilityCoeff[j];
                } else {
                    total += (actualValue / invertedProbability[j]);
                }
                expression.append(' ').append('(').append(actualValue).append('/')
                        .append(invertedProbability[j]).append(')').append(' ').append('+');
            }
            expression.deleteCharAt(expression.length() - 1);
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
