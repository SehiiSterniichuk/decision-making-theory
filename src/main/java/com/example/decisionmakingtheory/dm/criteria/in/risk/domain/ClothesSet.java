package com.example.decisionmakingtheory.dm.criteria.in.risk.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.time.Month;

public record ClothesSet(float deliveringPrice, List<List<Clothes>> necessaryClothes) {

    public List<String> getMonths() {
        return IntStream.range(0, necessaryClothes.size())
                .mapToObj(index -> {
                    int monthValue = index % 12;
                    return Month.of(monthValue + 1).toString();
                })
                .collect(Collectors.toList());
    }

    public static String clothesToString(List<Clothes> clothes) {
        return clothes.stream().filter(Objects::nonNull).map(Clothes::name).collect(Collectors.joining(","));
    }
}
