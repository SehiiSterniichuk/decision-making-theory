package com.example.decisionmakingtheory.containerpackingproblem.domain;

import java.util.Arrays;
import java.util.List;

public record Cargo(int[] goods) {
    public static Cargo combineAll(List<Cargo> cargos) {
        int size = cargos.stream().map(Cargo::goods).mapToInt(i -> i.length).sum();
        int[] newArray = new int[size];
        int target = 0;
        for (var c : cargos) {
            System.arraycopy(c.goods, 0, newArray, target, c.goods.length);
            target += c.goods.length;
        }
        return new Cargo(newArray);
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "goods=" + Arrays.toString(goods) +
                '}';
    }
}
