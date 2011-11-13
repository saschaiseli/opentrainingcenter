package ch.iseli.sportanalyzer.client.model;

public enum Units {
    BEATS_PER_MINUTE("bpm"), PACE("min/km"), KM("km"), HOUR_MINUTE_SEC("hh:mm:ss"), NONE("");

    private String name;

    private Units(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
