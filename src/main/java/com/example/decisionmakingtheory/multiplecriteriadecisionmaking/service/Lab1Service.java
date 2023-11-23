package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service;

import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.LabResult;

import java.util.List;

public interface Lab1Service {
    List<LabResult> results();

    List<LabResult> testResults(double z1x1, double z1x2, double z2x1, double z2x2, double r, double v);
}
