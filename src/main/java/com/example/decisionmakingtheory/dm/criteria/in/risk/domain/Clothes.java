package com.example.decisionmakingtheory.dm.criteria.in.risk.domain;

import java.util.function.Function;

public record Clothes(String name, float weight, float price) {
    public Clothes withNewPrice(Function<Float, Float> price){
        return new Clothes(this.name, this.weight, price.apply(this.price));
    }
}
