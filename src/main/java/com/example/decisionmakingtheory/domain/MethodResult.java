package com.example.decisionmakingtheory.domain;

import lombok.Builder;

@Builder
public record MethodResult(DominationView domination, String fileName, String methodName) {
}
