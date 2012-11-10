package ch.opentrainingcenter.model.training;

public enum Intervall {
    KLEINER_10(0), VON10_BIS_15(10), VON15_BIS_20(15), VON20_BIS_25(20), PLUS25(25);

    private final int von;

    private Intervall(final int von) {
        this.von = von;
    }

    public int getVon() {
        return von;
    }
}