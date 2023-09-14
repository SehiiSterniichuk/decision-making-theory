package com.example.decisionmakingtheory.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record LabResult(AlternativeCriteriaTable table, List<MethodResult> methods) {
}
