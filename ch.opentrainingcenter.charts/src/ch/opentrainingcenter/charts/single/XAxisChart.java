package ch.opentrainingcenter.charts.single;

import ch.opentrainingcenter.i18n.Messages;

/**
 * Enum welche die Granularität (X-Achse) der Charts definiert. Bei
 * {@link XAxisChart#MONTH} werden zum Beispiel alle Trainings innerhalb eines
 * Monates zu einem einzigen Training zusammengefasst.
 * 
 */
public enum XAxisChart {
    YEAR(4, Messages.ChartSerieType0, Messages.ChartSerieType8, Messages.ChartSerieType3), //
    YEAR_START_TILL_NOW(3, Messages.ChartSerieType9, Messages.ChartSerieType8, Messages.ChartSerieType3), //
    MONTH(2, Messages.ChartSerieType1, Messages.ChartSerieType2, Messages.ChartSerieType3), //
    WEEK(1, Messages.ChartSerieType4, Messages.ChartSerieType5, Messages.ChartSerieType6), //
    DAY(0, Messages.ChartSerieType7);

    private final int index;
    private final String name;
    private final String formatPattern;
    private final String label;

    private XAxisChart(final int index, final String name) {
        this.index = index;
        this.name = name;
        formatPattern = null;
        label = null;
    }

    private XAxisChart(final int index, final String name, final String formatPattern, final String label) {
        this.index = index;
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

    /**
     * @return den ChartFilter mit dem angegebenen Index.
     */
    public static XAxisChart getByIndex(final int index) {
        switch (index) {
        case 0:
            return XAxisChart.DAY;
        case 1:
            return XAxisChart.WEEK;
        case 2:
            return XAxisChart.MONTH;
        case 3:
            return XAxisChart.YEAR_START_TILL_NOW;
        case 4:
            return XAxisChart.YEAR;
        default:
            return XAxisChart.DAY;
        }
    }

    /**
     * @return String Array mit allen ChartFilter Namen.
     */
    public static String[] items() {
        final String[] result = new String[XAxisChart.values().length];
        for (final XAxisChart item : XAxisChart.values()) {
            result[item.index] = item.name;
        }
        return result;
    }
}
