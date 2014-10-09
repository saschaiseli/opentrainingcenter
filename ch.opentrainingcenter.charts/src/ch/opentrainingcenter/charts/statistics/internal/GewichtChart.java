package ch.opentrainingcenter.charts.statistics.internal;

import java.awt.Paint;

import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IHealth;

public class GewichtChart extends VitaldatenChartViewer {
    private final IPreferenceStore store;

    public GewichtChart(final AbstractCache<Integer, IHealth> cache, final IPreferenceStore store) {
        super(cache);
        this.store = store;
    }

    @Override
    protected Number getValue(final IHealth health) {
        return health.getWeight();
    }

    @Override
    protected String getYAchseBeschriftung() {
        return Messages.GewichtChart_0;
    }

    @Override
    protected String getChartTitle() {
        return Messages.GewichtChart_1;
    }

    @Override
    public String getTabName() {
        return Messages.GewichtChart_2;
    }

    @Override
    protected Paint getBarColor() {
        return ColorFromPreferenceHelper.getColor(store, PreferenceConstants.GEWICHT_COLOR, 200);
    }
}
