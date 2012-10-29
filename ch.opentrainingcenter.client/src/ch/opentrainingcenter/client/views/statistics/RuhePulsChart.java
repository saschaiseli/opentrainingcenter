package ch.opentrainingcenter.client.views.statistics;

import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteHealth;

public class RuhePulsChart extends VitaldatenChartViewer {

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
    protected String getTabName() {
        return Messages.RuhePulsChart_2;
    }
}
