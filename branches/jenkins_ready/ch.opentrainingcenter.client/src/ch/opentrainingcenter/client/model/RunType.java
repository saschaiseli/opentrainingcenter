package ch.opentrainingcenter.client.model;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.client.Messages;

public enum RunType {
    /**
     * Unbekannter Typ
     */
    NONE(0, Messages.RunType_0),
    /**
     * Gleich wie intensives Intervalltraining. Der Unterschied liegt in der Länge der Intervalle und der damit verbundenen geringeren Laufgeschwindigkeit. Beispiel: 5 Minuten
     * schnell, 2 Minuten Trabpause, 5 Minuten schnell etc. Extensive Intervalle werden bereits schon in der Aufbauetappe des Jahresplanes integriert.
     */
    EXT_INTERVALL(1, Messages.RunType_1),
    /**
     * Intervalltrainings werden in Serien gelaufen. Zum Beispiel: 6 x 200 m schnell, jeweils 2 Minuten Trabpause. Intervall bedeutet "Pause". Der Kreislauf wird belastet,
     * anschliessend erhält er Zeit, sich zum Teil wieder zu erholen. Dann folgt der nächste Intervall. Intensive Intervalltrainings werden meist in der Wettkampfvorbereitung
     * durchgeführt.
     */
    INT_INTERVALL(2, Messages.RunType_2),
    /**
     * Langer Dauerlauf 70-75% maximalen Herzfrequenz.
     */
    LONG_JOG(3, Messages.RunType_3),
    /**
     * letztes drittel 80-85% der maximalen Herzfrequenz, die ersten zwei drittel wie {@link RunType#LONG_JOG}
     */
    POWER_LONG_JOG(4, Messages.RunType_4),
    /**
     * Schneller Lauf z.B 8km in 40min
     */
    TEMPO_JOG(5, Messages.RunType_5);

    private final int index;
    private final String title;

    private RunType(final int index, final String title) {
        this.index = index;
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public static RunType getRunType(final int index) {
        for (final RunType type : RunType.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        throw new IllegalArgumentException("Der Typ mit dem Index: " + index + " existiert nicht!"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static String[] getAllTypes() {
        final List<String> typeTitles = new ArrayList<String>();
        for (final RunType type : RunType.values()) {
            typeTitles.add(type.getTitle());
        }
        return typeTitles.toArray(new String[0]);
    }
}
