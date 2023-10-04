package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain;

import java.util.List;

public record DominationView(List<String> a) {
    @SuppressWarnings("unused")
    public String collectResult() {
        var builder = new StringBuilder();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).isEmpty()) {
                builder.append("A").append(i + 1).append(",");
            }
        }
        int length = builder.length();
        builder.replace(length - 1, length, ";");
        return builder.toString();
    }
}
