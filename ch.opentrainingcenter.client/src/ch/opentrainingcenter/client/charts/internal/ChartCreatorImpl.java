package ch.opentrainingcenter.client.charts.internal;

import java.awt.Color;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.charts.ChartCreator;
import ch.opentrainingcenter.client.charts.HeartIntervallCreator;
import ch.opentrainingcenter.client.helper.ZoneHelper;
import ch.opentrainingcenter.client.helper.ZoneHelper.Zone;
import ch.opentrainingcenter.client.views.overview.ChartType;

public class ChartCreatorImpl implements ChartCreator {

    private final Cache cache;
    private final HeartIntervallCreator heartIntervall;

    public ChartCreatorImpl(final Cache cache, final IPreferenceStore store) {
        this.cache = cache;
        heartIntervall = new HeartIntervallCreator(store);
    }

    @Override
    public JFreeChart createChart(final XYDataset dataset, final ChartType type) {
        final JFreeChart chart = ChartFactory.createXYLineChart(type.getTitel(), // chart
                // title
                type.getxAchse(), // x axis label
                type.getyAchse(), // y axis label
                dataset, // data
                PlotOrientation.VERTICAL, false, // include legend
                true, // tooltips
                false // urls
                );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        final XYLineAndShapeRenderer renderer = new XYSplineRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        setLowerAndUpperBounds(plot);
        if (ChartType.HEART_DISTANCE.equals(type)) {
            addIntervallMarker(plot);
        }
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }

    private void setLowerAndUpperBounds(final XYPlot plot) {
        final ValueAxis axis = plot.getRangeAxis();
        final Range dataRange = plot.getDataRange(axis);
        if (dataRange != null) {
            axis.setLowerBound(dataRange.getLowerBound() * 0.95);
            axis.setUpperBound(dataRange.getUpperBound() * 1.05);
        }
        plot.setRangeAxis(axis);
    }

    private void addIntervallMarker(final XYPlot plot) {
        final Map<Zone, IntervalMarker> markers = heartIntervall.createMarker(cache.getSelectedProfile());
        plot.addRangeMarker(markers.get(ZoneHelper.Zone.AEROBE));
        plot.addRangeMarker(markers.get(ZoneHelper.Zone.SCHWELLE));
        plot.addRangeMarker(markers.get(ZoneHelper.Zone.ANAEROBE));
    }

}
