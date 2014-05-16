package ch.opentrainingcenter.transfer;

public enum Sport {
    RUNNING(0, "RUNNING"),

    BIKE(1, "Bike"),

    OTHER(2, "other");

    private final int index;
    private final String message;

    private Sport(int index, String message) {
        this.index = index;
        this.message = message;
    }

    public int getIndex() {
        return index;
    }

    public String getMessage() {
        return message;
    }
}
