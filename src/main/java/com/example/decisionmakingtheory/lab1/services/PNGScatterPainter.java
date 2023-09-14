package com.example.decisionmakingtheory.lab1.services;

import com.example.decisionmakingtheory.lab1.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.lab1.domain.Domination;
import com.example.decisionmakingtheory.lab1.domain.PlotData;

import java.io.File;

public interface PNGScatterPainter {

    void drawScatter(File file, AlternativeCriteriaTable table, Domination domination, PlotData data);
}
