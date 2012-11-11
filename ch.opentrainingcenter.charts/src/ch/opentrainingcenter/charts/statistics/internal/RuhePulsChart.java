package ch.opentrainingcenter.charts.statistics.internal;

import java.awt.Paint;

import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;

public class RuhePulsChart extends VitaldatenChartViewer {

    private final IPreferenceStore store;

    public RuhePulsChart(final AbstractCache<Integer, ConcreteHealth> cache, final IPreferenceStore store) {
        super(cache);
        this.store = store;
    }

    @Override
    protected Number getValue(final ConcreteHealth health) {
        return health.getCardio();
    }

    @Override
    protected String getYAchseBeschriftung() {
        return Messages.RuhePulsChart_0;
    }

    @Override
    protected String getChartTitle() {
        return Messages.RuhePulsChart_1;
    }

    @Override
    public String getTabName() {
        return Messages.RuhePulsChart_2;
    }

    @Override
    protected Paint getBarColor() {
        return ColorFromPreferenceHelper.getColor(store, PreferenceConstants.RUHEPULS_COLOR, 200);
    }
}
