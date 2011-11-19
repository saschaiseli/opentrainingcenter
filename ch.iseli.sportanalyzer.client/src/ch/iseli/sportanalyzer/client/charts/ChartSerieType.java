package ch.iseli.sportanalyzer.client.charts;

public enum ChartSerieType {
    YEAR("Jahr"), MONTH("Monat"), WEEK("Woche"), DAY("Tag");

    private final String name;

    private ChartSerieType(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }
}
