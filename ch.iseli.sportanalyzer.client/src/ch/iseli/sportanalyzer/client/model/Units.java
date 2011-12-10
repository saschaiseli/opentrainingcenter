package ch.iseli.sportanalyzer.client.model;

import ch.iseli.sportanalyzer.client.Messages;

public enum Units {
    BEATS_PER_MINUTE(Messages.Units_0), PACE(Messages.Units_1), KM(Messages.Units_2), HOUR_MINUTE_SEC(Messages.Units_3), NONE(Messages.Units_4);

    private String name;

    private Units(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
