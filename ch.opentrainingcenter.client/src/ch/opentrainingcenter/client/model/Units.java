package ch.opentrainingcenter.client.model;

import ch.opentrainingcenter.client.Messages;

public enum Units {
    BEATS_PER_MINUTE(Messages.Units0), //
    PACE(Messages.Units1), //
    KM(Messages.Units2), //
    HOUR_MINUTE_SEC(Messages.Units3), //
    NONE(Messages.Units4);

    private String name;

    private Units(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
