package ch.opentrainingcenter.charts.single.creators;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import ch.opentrainingcenter.charts.single.ChartType;

public interface ChartCreator {
    JFreeChart createChart(final XYDataset dataset, final ChartType type);
}
