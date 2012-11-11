package ch.opentrainingcenter.core.helper;

import ch.opentrainingcenter.i18n.Messages;

public enum ChartSerieType {
    YEAR(Messages.ChartSerieType0, Messages.ChartSerieType8, Messages.ChartSerieType3), //
    MONTH(Messages.ChartSerieType1, Messages.ChartSerieType2, Messages.ChartSerieType3), //
    WEEK(Messages.ChartSerieType4, Messages.ChartSerieType5, Messages.ChartSerieType6), //
    DAY(Messages.ChartSerieType7);

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
