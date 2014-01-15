package ch.opentrainingcenter.core.helper;

import ch.opentrainingcenter.i18n.Messages;

/**
 * Enum welche die Granularität (X-Achse) der Charts definiert. Bei
 * {@link ChartSerieType#MONTH} werden zum Beispiel alle Trainings innerhalb
 * eines Monates zu einem einzigen Training zusammengefasst.
 * 
 */
public enum ChartSerieType {
    YEAR(3, Messages.ChartSerieType0, Messages.ChartSerieType8, Messages.ChartSerieType3), //
    MONTH(2, Messages.ChartSerieType1, Messages.ChartSerieType2, Messages.ChartSerieType3), //
    WEEK(1, Messages.ChartSerieType4, Messages.ChartSerieType5, Messages.ChartSerieType6), //
    DAY(0, Messages.ChartSerieType7);

    private final int index;
    private final String name;
    private final String formatPattern;
    private final String label;

    private ChartSerieType(final int index, final String name) {
        this.index = index;
        this.name = name;
        formatPattern = null;
        label = null;
    }

    private ChartSerieType(final int index, final String name, final String formatPattern, final String label) {
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
    public static ChartSerieType getByIndex(final int index) {
        switch (index) {
        case 0:
            return ChartSerieType.DAY;
        case 1:
            return ChartSerieType.WEEK;
        case 2:
            return ChartSerieType.MONTH;
        case 3:
            return ChartSerieType.YEAR;
        default:
            return ChartSerieType.DAY;
        }
    }

    /**
     * @return String Array mit allen ChartFilter Namen.
     */
    public static String[] items() {
        final String[] result = new String[ChartSerieType.values().length];
        for (final ChartSerieType item : ChartSerieType.values()) {
            result[item.index] = item.name;
        }
        return result;
    }
}
