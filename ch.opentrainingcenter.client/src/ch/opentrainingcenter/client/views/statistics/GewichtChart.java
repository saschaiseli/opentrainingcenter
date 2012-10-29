package ch.opentrainingcenter.client.views.statistics;

import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteHealth;

public class GewichtChart extends VitaldatenChartViewer {

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

}
