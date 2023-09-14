package com.example.decisionmakingtheory.lab1.services.implementation;

import com.example.decisionmakingtheory.lab1.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.lab1.domain.Domination;
import com.example.decisionmakingtheory.lab1.domain.PlotData;
import com.example.decisionmakingtheory.lab1.services.PNGScatterPainter;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

@Component
@Slf4j
public class JFreeChartPNGScatterPainter implements PNGScatterPainter {

    record Point(int x, int y) {
        public double distanceTo(Point other) {
            int deltaX = this.x() - other.x();
            int deltaY = this.y() - other.y();
            return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        }
        public static List<Point> sortNearestNeighbor(List<Point> points) {
            int n = points.size();
            if (n <= 1) {
                return points;
            }
            List<Point> sortedPoints = new ArrayList<>(n);
            boolean[] visited = new boolean[n];
            Point start = points.get(0);
            sortedPoints.add(start);
            visited[0] = true;
            while (sortedPoints.size() < n) {
                Point currentPoint = sortedPoints.get(sortedPoints.size() - 1);
                int nextPointIndex = -1;
                double minDistance = Double.MAX_VALUE;
                for (int j = 0; j < n; j++) {
                    if (!visited[j]) {
                        double distance = currentPoint.distanceTo(points.get(j));
                        if (distance < minDistance) {
                            minDistance = distance;
                            nextPointIndex = j;
                        }
                    }
                }
                if (nextPointIndex != -1) {
                    visited[nextPointIndex] = true;
                    sortedPoints.add(points.get(nextPointIndex));
                }
            }
            return sortedPoints;
        }
    }

    public JFreeChart createScatter(int[][] alternatives, int[] domination, PlotData data) {
        NumberAxis domain = new NumberAxis("Q1");
        NumberAxis range = new NumberAxis("Q2");
        domain.setRange(-2, 12);
        range.setRange(-2, 12);
        XYDotRenderer renderer = new XYDotRenderer();
        renderer.setDotHeight(7);
        renderer.setDotWidth(7);
        int length = alternatives.length;
        var alphaSolutions = Arrays.stream(domination).filter(x -> x == -1).count();
        long betaSolutions = domination.length - alphaSolutions;
        LabeledXYDataset dataset = new LabeledXYDataset(List.of(alphaSolutions, betaSolutions), data.series());
        List<XYTextAnnotation> annotations = new ArrayList<>(length);
        Map<Point, XYTextAnnotation> pointMap = new HashMap<>();
        List<Point> solutionPoints = new ArrayList<>((int) alphaSolutions);
        for (int i = 0; i < length; i++) {
            var dominationValue = domination[i];
            boolean isSolution = dominationValue == -1;
            Point point = new Point(alternatives[i][0], alternatives[i][1]);
            int seriesId = isSolution ? 0 : 1;
            if (isSolution && !pointMap.containsKey(point)) {
                solutionPoints.add(point);
            }
            dataset.add(seriesId, point);
            var oldLabel = pointMap.get(point);
            String label = "A" + (i + 1);
            if (oldLabel == null) {
                double diff = -0.25;
                XYTextAnnotation pLabel = new XYTextAnnotation(label, point.x() + diff, point.y() + diff);
                pLabel.setTextAnchor(TextAnchor.CENTER_RIGHT);
                Color col = isSolution ? Color.green : Color.red;
                pLabel.setPaint(col);
                Font labelFont = new Font(Font.MONOSPACED, Font.ITALIC, 16);
                pLabel.setFont(labelFont);
                annotations.add(pLabel);
                pointMap.put(point, pLabel);
                continue;
            }
            String oldLabelText = oldLabel.getText();
            oldLabel.setText(oldLabelText + "=" + label);
        }
        renderer.setSeriesPaint(0, Color.green);
        renderer.setSeriesPaint(1, Color.red);
        XYSeries lineSeries = new XYSeries("Line");
        List<Point> points = Point.sortNearestNeighbor(solutionPoints);
        points.forEach(p -> lineSeries.add(p.x(), p.y()));
        XYSeriesCollection lineDataset = new XYSeriesCollection(lineSeries);
        // Create the line renderer
        XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
        lineRenderer.setSeriesPaint(0, Color.green); // Set the line color

        XYPlot plot = new XYPlot(dataset, domain, range, renderer);
        plot.setDataset(1, lineDataset); // Add the line dataset to the plot
        plot.setRenderer(1, lineRenderer); // Set the line renderer
        annotations.forEach(plot::addAnnotation);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        renderer.setBaseSeriesVisible(true);
        return new JFreeChart(
                data.title(), JFreeChart.DEFAULT_TITLE_FONT, plot, false);
    }

    @Override
    public void drawScatter(File file, AlternativeCriteriaTable table, Domination domination, PlotData data) {
        JFreeChart chart = createScatter(table.alternatives(), domination.a(), data);
        try {
            ChartUtilities.saveChartAsPNG(file, chart, data.width(), data.height());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class LabeledXYDataset extends AbstractXYDataset {
        private final List<List<Point>> series;
        private final String seriesName;

        public LabeledXYDataset(List<Number> seriesSizes, String seriesName) {
            int seriesNumber = seriesSizes.size();
            this.series = new ArrayList<>(seriesNumber);
            for (int i = 0; i < seriesNumber; i++) {
                Number size = seriesSizes.get(i);
                series.add(new ArrayList<>(size.intValue()));
            }
            this.seriesName = seriesName;
        }

        public void add(int seriesId, Point p) {
            this.series.get(seriesId).add(p);
        }

        @Override
        public int getSeriesCount() {
            return series.size();
        }

        @Override
        public Comparable<?> getSeriesKey(int series) {
            return seriesName;
        }

        @Override
        public int getItemCount(int series) {
            return this.series.get(series).size();
        }

        @Override
        public Number getX(int series, int item) {
            return this.series.get(series).get(item).x();
        }

        @Override
        public Number getY(int series, int item) {
            return this.series.get(series).get(item).y();
        }
    }
}
