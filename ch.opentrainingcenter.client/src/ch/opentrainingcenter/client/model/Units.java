package ch.opentrainingcenter.client.model;

import ch.opentrainingcenter.i18n.Messages;

public enum Units {
    BEATS_PER_MINUTE(Messages.Units0), //
    PACE(Messages.Units1), //
    GESCHWINDIGKEIT(Messages.Units5), //
    KM(Messages.Units2), //
    HOUR_MINUTE_SEC(Messages.Units3), //
    METER(Messages.Units_0), //
    NONE(Messages.Units4);

    private String name;

    private Units(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
