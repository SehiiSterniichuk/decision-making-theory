package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service;

import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.Domination;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.PlotData;

import java.io.File;

public interface PNGScatterPainter {

    void drawScatter(File file, AlternativeCriteriaTable table, Domination domination, PlotData data);
}
