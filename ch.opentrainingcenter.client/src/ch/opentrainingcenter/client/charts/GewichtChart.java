package ch.opentrainingcenter.client.charts;

import java.awt.Paint;

import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.client.views.statistics.VitaldatenChartViewer;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;

public class GewichtChart extends VitaldatenChartViewer {
    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    @Override
    protected Number getValue(final ConcreteHealth health) {
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
    protected String getTabName() {
        return Messages.GewichtChart_2;
    }

    @Override
    protected Paint getBarColor() {
        return ColorFromPreferenceHelper.getColor(store, PreferenceConstants.GEWICHT_COLOR, 200);
    }
}
