package ch.opentrainingcenter.client.charts;

import java.awt.Color;
import java.awt.Paint;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.cache.ICache;
import ch.opentrainingcenter.client.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.client.model.planing.impl.KwJahrKey;
import ch.opentrainingcenter.client.model.planing.impl.PlanungModel;

public class WeekXYBarRenderer extends XYBarRenderer implements XYItemRenderer {

    private static final int ALPHA = 255;

    private static final long serialVersionUID = -7407945301726330129L;

    private final IntervalXYDataset dataset;

    private final Color colorNotDefined;

    private final Color colorBelow;

    private final Color colorAbove;

    private final ICache<KwJahrKey, PlanungModel> cache;

    public WeekXYBarRenderer(final IntervalXYDataset dataset, final IPreferenceStore store, final ICache<KwJahrKey, PlanungModel> cache) {
        this.dataset = dataset;
        this.cache = cache;
        colorNotDefined = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.KM_PER_WEEK_COLOR_NOT_DEFINED, ALPHA);
        colorBelow = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.KM_PER_WEEK_COLOR_BELOW, ALPHA);
        colorAbove = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.KM_PER_WEEK_COLOR_ABOVE, ALPHA);
    }

    @Override
    public Paint getItemPaint(final int series, final int item) {
        final Number y = dataset.getY(series, item);
        final Number x = dataset.getX(series, item);
        final DateTime date = new DateTime(x.longValue());
        final KwJahrKey key = new KwJahrKey(date.getYear(), date.getWeekOfWeekyear());
        final PlanungModel plan = cache.get(key);
        if (plan != null) {
            if (y != null && y.doubleValue() < plan.getKmProWoche()) {
                return colorBelow;
            } else {
                return colorAbove;
            }
        } else {
            return colorNotDefined;
        }
    }
}
