package com.example.decisionmakingtheory.dm.criteria.in.risc.domain;

public record Alternative(byte min,
                          byte max,
                          String hat,
                          String outerwear,
                          String gloves,
                          String trousers,
                          String boots,
                          float weight
) {
}
