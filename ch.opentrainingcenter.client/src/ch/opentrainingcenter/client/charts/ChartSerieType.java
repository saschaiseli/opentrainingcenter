package ch.opentrainingcenter.client.charts;

import ch.opentrainingcenter.client.Messages;

public enum ChartSerieType {
    YEAR(Messages.ChartSerieType_0, Messages.ChartSerieType_8, Messages.ChartSerieType_3), MONTH(Messages.ChartSerieType_1, Messages.ChartSerieType_2, Messages.ChartSerieType_3), WEEK(
            Messages.ChartSerieType_4, Messages.ChartSerieType_5, Messages.ChartSerieType_6), DAY(Messages.ChartSerieType_7);

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
