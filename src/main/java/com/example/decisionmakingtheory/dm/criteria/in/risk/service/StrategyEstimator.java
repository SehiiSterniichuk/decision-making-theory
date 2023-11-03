package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.Alternative;
import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.Clothes;
import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.ClothesSet;
import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.PriceAlternativeTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StrategyEstimator {
    public PriceAlternativeTable buyNecessaryClothes(List<Clothes> clothes,
                                                     List<Alternative> alternatives,
                                                     List<Byte> weather,
                                                     float weightCoeff) {
        var map = new TemperatureAlternatives(alternatives);
        List<ClothesSet> set = new ArrayList<>(alternatives.size());
        Map<String, Clothes> clothesMap = clothes.stream()
                .collect(Collectors.toMap(Clothes::name, c -> c));
        for (var alternative : alternatives) {
            List<List<Clothes>> necessaryClothes = new ArrayList<>(weather.size());
            for (var temp : weather) {
                var properAlternative = map.getAlternativeForTemperature(temp);

                if (properAlternative.equals(alternative)) {
                    necessaryClothes.add(List.of());
                    continue;
                }
                List<Clothes> necessary = getNecessaryClothes(alternative, properAlternative, clothesMap);
                necessaryClothes.add(necessary);
            }
            set.add(new ClothesSet(alternative.weight() * weightCoeff, necessaryClothes));
        }
        return new PriceAlternativeTable(set);
    }

    private static List<Clothes> getNecessaryClothes(Alternative alternative, Alternative properAlternative, Map<String, Clothes> clothesMap) {
        List<Clothes> necessary = new ArrayList<>();
        if (!properAlternative.equalHats(alternative)) {
            var newValue = properAlternative.hat();
            if (!newValue.isBlank()) {
                necessary.add(clothesMap.get(newValue));
            }
        }
        if (!properAlternative.equalOuterwear(alternative)) {
            var newValue = properAlternative.outerwear();
            if (!newValue.isBlank()) {
                necessary.add(clothesMap.get(newValue));
            }
        }
        if (!properAlternative.equalTrousers(alternative)) {
            var newValue = properAlternative.trousers();
            if (!newValue.isBlank()) {
                necessary.add(clothesMap.get(newValue));
            }
        }
        if (!properAlternative.equalGloves(alternative)) {
            var newValue = properAlternative.gloves();
            if (!newValue.isBlank()) {
                necessary.add(clothesMap.get(newValue));
            }
        }
        if (!properAlternative.equalBoots(alternative)) {
            var newValue = properAlternative.boots();
            if (!newValue.isBlank()) {
                necessary.add(clothesMap.get(newValue));
            }
        }
        return necessary;
    }
}
