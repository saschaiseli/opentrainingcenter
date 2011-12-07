package ch.iseli.sportanalyzer.client.charts;

public enum ChartSerieType {
    YEAR("Jahr"), MONTH("Monat", "MMMMM", ""), WEEK("Woche", "w", "KW"), DAY("Tag");

    private final String name;
    private final String formatPattern;
    private final String label;

    private ChartSerieType(final String name) {
        this.name = name;
        formatPattern = null;
        label = null;
    }

    private ChartSerieType(final String name, final String formatPattern, final String label) {
        this.name = name;
        this.formatPattern = formatPattern;
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getFormatPattern() {
        return formatPattern;
    }

    public boolean isLabelVisible() {
        return formatPattern != null;
    }
}
