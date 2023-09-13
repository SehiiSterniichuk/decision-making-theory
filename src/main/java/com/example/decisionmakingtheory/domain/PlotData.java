package com.example.decisionmakingtheory.domain;

import lombok.Builder;

@Builder
public record PlotData(String title, String series, String xAxis, String yAxis, int width, int height) {
}
