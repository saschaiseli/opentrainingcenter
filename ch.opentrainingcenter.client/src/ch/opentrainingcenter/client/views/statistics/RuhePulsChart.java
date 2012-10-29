package ch.opentrainingcenter.client.views.statistics;

import ch.opentrainingcenter.client.model.navigation.impl.ConcreteHealth;

public class RuhePulsChart extends VitaldatenChartViewer {

    @Override
    protected Number getValue(final ConcreteHealth health) {
        return health.getCardio();
    }

    @Override
    protected String getYAchseBeschriftung() {
        return "Puls [bpm]";
    }

    @Override
    protected String getChartTitle() {
        return "morgendlicher Ruhepuls";
    }

    @Override
    protected String getTabName() {
        return "Ruhepuls";
    }
}
