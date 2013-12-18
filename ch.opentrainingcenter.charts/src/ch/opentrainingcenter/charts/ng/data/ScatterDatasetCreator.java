package ch.opentrainingcenter.charts.ng.data;

import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Bereitet Dataset fuer Scatterplots auf.
 * 
 */
public final class ScatterDatasetCreator {

    private ScatterDatasetCreator() {

    }

    /**
     * Erstellt ein Dataset fuer {@link JFreeChart}
     * 
     * @param all
     *            die aeussere Map ist die Serie. Der Key hierzu ist der Name
     *            der Serie und der Wert erneut eine Map mit dem X-Value als key
     *            und dem Y-Value als value.
     * @return ein {@link XYDataset}
     */
    public static XYDataset createDataset(final Map<String, Map<Integer, Integer>> all) {
        final XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        for (final Map.Entry<String, Map<Integer, Integer>> singleRun : all.entrySet()) {
            final XYSeries series = new XYSeries(singleRun.getKey());
            for (final Map.Entry<Integer, Integer> entry : singleRun.getValue().entrySet()) {
                final double x = entry.getKey();
                final double y = entry.getValue();
                series.add(x, y);
            }
            xySeriesCollection.addSeries(series);
        }

        return xySeriesCollection;
    }
}
