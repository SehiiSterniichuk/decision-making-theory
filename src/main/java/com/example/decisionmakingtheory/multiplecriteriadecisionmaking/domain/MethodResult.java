package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain;

import lombok.Builder;

@Builder
public record MethodResult(DominationView domination, String fileName, String methodName) {
}
