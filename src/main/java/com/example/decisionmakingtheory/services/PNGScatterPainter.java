package com.example.decisionmakingtheory.services;

import com.example.decisionmakingtheory.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.domain.Domination;
import com.example.decisionmakingtheory.domain.PlotData;

import java.io.File;

public interface PNGScatterPainter {

    void drawScatter(File file, AlternativeCriteriaTable table, Domination domination, PlotData data);
}
