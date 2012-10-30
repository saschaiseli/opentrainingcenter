package ch.opentrainingcenter.client.charts;

import java.awt.Color;
import java.awt.Paint;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.IntervalXYDataset;

import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.helper.ColorFromPreferenceHelper;

public class WeekXYBarRenderer extends XYBarRenderer implements XYItemRenderer {

    private static final double INTERVALL_MARKER_BREITE = 0.01;

    private static final int ALPHA = 255;

    private static final long serialVersionUID = -7407945301726330129L;

    private final IntervalXYDataset dataset;

    private final int targetKmPerWeek;

    private final Color colorBelow;

    private final Color colorAbove;

    public WeekXYBarRenderer(final IntervalXYDataset dataset, final XYPlot plot, final IPreferenceStore store) {
        this.dataset = dataset;
        targetKmPerWeek = store.getInt(PreferenceConstants.KM_PER_WEEK);

        colorBelow = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.KM_PER_WEEK_COLOR_BELOW, ALPHA);
        colorAbove = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.KM_PER_WEEK_COLOR_ABOVE, ALPHA);

        final IntervalMarker below = new IntervalMarker(targetKmPerWeek - INTERVALL_MARKER_BREITE, targetKmPerWeek + INTERVALL_MARKER_BREITE);
        below.setPaint(colorBelow);
        below.setAlpha(1f);

        plot.addRangeMarker(below);

    }

    @Override
    public Paint getItemPaint(final int series, final int item) {
        final Number y = dataset.getY(series, item);
        if (y != null && y.doubleValue() < targetKmPerWeek) {
            return colorBelow;
        } else {
            return colorAbove;
        }
    }
}
