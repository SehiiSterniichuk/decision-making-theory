package com.example.decisionmakingtheory.lab1.domain;

import lombok.Builder;

@Builder
public record MethodResult(DominationView domination, String fileName, String methodName) {
}
