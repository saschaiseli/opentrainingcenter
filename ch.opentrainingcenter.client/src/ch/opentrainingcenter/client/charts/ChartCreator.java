package ch.opentrainingcenter.client.charts;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import ch.opentrainingcenter.client.views.overview.ChartType;

public interface ChartCreator {
    JFreeChart createChart(final XYDataset dataset, final ChartType type);
}
