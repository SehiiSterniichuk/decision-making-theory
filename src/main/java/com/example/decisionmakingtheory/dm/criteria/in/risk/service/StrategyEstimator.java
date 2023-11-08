package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StrategyEstimator {
    public PriceAlternativeTable buyNecessaryClothes(Map<String, Clothes> clothesMap,
                                                     List<Alternative> alternatives,
                                                     List<Byte> weather,
                                                     float weightCoeff) {
        var map = new TemperatureAlternatives(alternatives);
        List<ClothesSet> set = new ArrayList<>(alternatives.size());
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
            List<MonthNewClothes> necessary = getMonthNewClothes(necessaryClothes);
            set.add(new ClothesSet(alternative.weight() * weightCoeff, necessary));
        }
        return new PriceAlternativeTable(set);
    }

    private static List<MonthNewClothes> getMonthNewClothes(List<List<Clothes>> necessaryClothes) {
        List<MonthNewClothes> necessary = new ArrayList<>(necessaryClothes.size());
        for (var list : necessaryClothes) {
            var formula = new StringBuilder();
            float total = 0;
            for (var item : list) {
                if(item == null){
                    continue;
                }
                float price = item.price();
                total += price;
                formula.append(' ').append(price).append(' ').append("+");
            }
            if(!formula.isEmpty()){
                formula.deleteCharAt(formula.length() - 1);
            }
            if(!formula.isEmpty()){
                formula.append('=').append(' ').append(total);
            }
            necessary.add(new MonthNewClothes(list, formula.toString(), total));
        }
        return necessary;
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
