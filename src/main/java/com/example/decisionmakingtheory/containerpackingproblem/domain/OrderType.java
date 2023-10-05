package com.example.decisionmakingtheory.containerpackingproblem.domain;


public enum OrderType {
    UNORDERED, ASCENDING, DESCENDING;

    public OrderType getOpposite() {
        return switch (this) {
            case ASCENDING -> DESCENDING;
            case UNORDERED -> UNORDERED;
            case DESCENDING -> ASCENDING;
        };
    }
}
