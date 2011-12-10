package ch.iseli.sportanalyzer.client.views.overview;

import ch.iseli.sportanalyzer.client.Messages;

public enum ChartType {
    HEART_DISTANCE(Messages.ChartType_0, Messages.ChartType_1, Messages.ChartType_2), //
    SPEED_DISTANCE(Messages.ChartType_3, Messages.ChartType_4, Messages.ChartType_5), //
    ALTITUDE_DISTANCE(Messages.ChartType_6, Messages.ChartType_7, Messages.ChartType_8);

    private final String titel;
    private final String yAchse;
    private final String xAchse;

    private ChartType(String titel, String yAchse, String xAchse) {
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
