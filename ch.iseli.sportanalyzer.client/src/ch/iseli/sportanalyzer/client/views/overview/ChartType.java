package ch.iseli.sportanalyzer.client.views.overview;

public enum ChartType {
    HEART_DISTANCE("Herzfrequenz in f(m)", "Herzfrequenz", "Distanz[m]"), //
    SPEED_DISTANCE("Pace in f(m)", "Pace[min/km]", "Distanz[m]"), //
    ALTITUDE_DISTANCE("Höhe in f(m)", "Höhe[m]", "Distanz[m]");

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
