package ch.opentrainingcenter.model.planing;

import ch.opentrainingcenter.core.PreferenceConstants;

public enum PlanungStatus {

    ERFOLGREICH(1, PreferenceConstants.ZIEL_ERFUELLT_COLOR), //
    NICHT_ERFOLGREICH(2, PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR), //
    UNBEKANNT(3, PreferenceConstants.ZIEL_NICHT_BEKANNT_COLOR);

    private final int id;
    private final String colorPreference;

    private PlanungStatus(final int id, final String colorPreference) {
        this.id = id;
        this.colorPreference = colorPreference;
    }

    public String getColorPreference() {
        return colorPreference;
    }

    public int getId() {
        return id;
    }

}
