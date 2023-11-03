package com.example.decisionmakingtheory.dm.criteria.in.risk.service;

import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.Alternative;
import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.Clothes;
import com.example.decisionmakingtheory.dm.criteria.in.risk.domain.PriceAlternativeTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return estimator.buyNecessaryClothes(clothes, alternatives, temperatures, coeff);
    }
}
