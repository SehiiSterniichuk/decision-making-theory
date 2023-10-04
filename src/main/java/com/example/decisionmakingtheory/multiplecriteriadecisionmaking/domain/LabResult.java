package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record LabResult(AlternativeCriteriaTable table, List<MethodResult> methods) {
}
