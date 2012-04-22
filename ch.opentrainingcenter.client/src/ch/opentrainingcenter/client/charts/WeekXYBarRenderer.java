package ch.opentrainingcenter.client.charts;

import java.awt.Color;
import java.awt.Paint;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.IntervalXYDataset;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.helper.ColorFromPreferenceHelper;

public class WeekXYBarRenderer extends XYBarRenderer implements XYItemRenderer {

    private static final long serialVersionUID = -7407945301726330129L;

    private final IntervalXYDataset dataset;

    private final int targetKmPerWeek;

    private final Color colorBelow;

    private final Color colorAbove;

    public WeekXYBarRenderer(final IntervalXYDataset dataset, final XYPlot plot) {
        this.dataset = dataset;
        final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
        targetKmPerWeek = preferenceStore.getInt(PreferenceConstants.KM_PER_WEEK);

        colorBelow = ColorFromPreferenceHelper.getColor(PreferenceConstants.KM_PER_WEEK_COLOR_BELOW, 255);
        colorAbove = ColorFromPreferenceHelper.getColor(PreferenceConstants.KM_PER_WEEK_COLOR_ABOVE, 255);

        final IntervalMarker below = new IntervalMarker(targetKmPerWeek - 0.01, targetKmPerWeek + 0.01);
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