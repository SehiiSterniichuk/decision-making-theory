package com.example.decisionmakingtheory.dm.criteria.in.risk.domain;

import lombok.Builder;

@Builder
public record Alternative(byte min,
                          byte max,
                          String hat,
                          String outerwear,
                          String gloves,
                          String trousers,
                          String boots,
                          float weight
) {
    public boolean equalHats(Alternative alternative) {
        return this.hat.equals(alternative.hat);
    }

    public boolean equalOuterwear(Alternative alternative) {
        return this.outerwear.equals(alternative.outerwear);
    }

    public boolean equalGloves(Alternative alternative) {
        return this.gloves.equals(alternative.gloves);
    }
    public boolean equalTrousers(Alternative alternative) {
        return this.trousers.equals(alternative.trousers);
    }

    public boolean equalBoots(Alternative alternative) {
        return this.boots.equals(alternative.boots);
    }
}
