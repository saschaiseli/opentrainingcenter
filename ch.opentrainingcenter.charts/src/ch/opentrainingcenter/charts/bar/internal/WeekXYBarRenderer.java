package ch.opentrainingcenter.charts.bar.internal;

import java.awt.Color;
import java.awt.Paint;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.joda.time.DateTime;

import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.ICache;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.planing.KwJahrKey;

public class WeekXYBarRenderer extends XYBarRenderer implements XYItemRenderer {

    private static final int ALPHA = 255;

    private static final long serialVersionUID = -7407945301726330129L;

    private final IntervalXYDataset dataset;

    private final Color colorNotDefined;

    private final Color colorBelow;

    private final Color colorAbove;

    private final ICache<KwJahrKey, IPlanungModel> cache;

    public WeekXYBarRenderer(final IntervalXYDataset dataset, final IPreferenceStore store, final ICache<KwJahrKey, IPlanungModel> cache) {
        this.dataset = dataset;
        this.cache = cache;
        colorNotDefined = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.ZIEL_NICHT_BEKANNT_COLOR, ALPHA);
        colorBelow = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR, ALPHA);
        colorAbove = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.ZIEL_ERFUELLT_COLOR, ALPHA);
    }

    @Override
    public Paint getItemPaint(final int series, final int item) {
        final Number y = dataset.getY(series, item);
        final Number x = dataset.getX(series, item);
        final DateTime date = new DateTime(x.longValue());
        final KwJahrKey key = new KwJahrKey(date.getYear(), date.getWeekOfWeekyear());
        final IPlanungModel plan = cache.get(key);
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
