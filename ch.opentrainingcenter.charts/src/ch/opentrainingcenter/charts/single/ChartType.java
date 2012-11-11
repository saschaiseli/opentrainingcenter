package ch.opentrainingcenter.charts.single;

import ch.opentrainingcenter.i18n.Messages;

public enum ChartType {
    HEART_DISTANCE(Messages.ChartType0, Messages.ChartType1, Messages.ChartType2), //
    SPEED_DISTANCE(Messages.ChartType3, Messages.ChartType4, Messages.ChartType5), //
    ALTITUDE_DISTANCE(Messages.ChartType6, Messages.ChartType7, Messages.ChartType8);

    private final String titel;
    private final String yAchse;
    private final String xAchse;

    private ChartType(final String titel, final String yAchse, final String xAchse) {
        this.titel = titel;
        this.yAchse = yAchse;
        this.xAchse = xAchse;
    }

    public String getTitel() {
        return titel;
    }

    public String getxAchse() {
        return xAchse;
    }

    public String getyAchse() {
        return yAchse;
    }

}
