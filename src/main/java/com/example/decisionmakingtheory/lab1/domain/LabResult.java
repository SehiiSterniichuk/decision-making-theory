package com.example.decisionmakingtheory.lab1.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record LabResult(AlternativeCriteriaTable table, List<MethodResult> methods) {
}
