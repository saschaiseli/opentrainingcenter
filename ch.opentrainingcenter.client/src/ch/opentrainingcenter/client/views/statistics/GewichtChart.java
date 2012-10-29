package ch.opentrainingcenter.client.views.statistics;

import ch.opentrainingcenter.client.model.navigation.impl.ConcreteHealth;

public class GewichtChart extends VitaldatenChartViewer {

    @Override
    protected Number getValue(final ConcreteHealth health) {
        return health.getWeight();
    }

    @Override
    protected String getYAchseBeschriftung() {
        return "Gewicht [kg]";
    }

    @Override
    protected String getChartTitle() {
        return "morgendliches Gewicht";
    }

    @Override
    protected String getTabName() {
        return "Gewicht";
    }

}
