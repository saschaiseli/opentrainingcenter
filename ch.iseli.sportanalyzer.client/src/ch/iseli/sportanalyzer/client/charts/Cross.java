package ch.iseli.sportanalyzer.client.charts;

import java.awt.Polygon;

public final class Cross extends Polygon {
    private static final long serialVersionUID = 1L;

    private static final Cross INSTANCE = new Cross();

    private Cross() {
        this.addPoint(0, 0);
        this.addPoint(2, 2);
        this.addPoint(0, 0);
        this.addPoint(-2, 2);
        this.addPoint(0, 0);
        this.addPoint(-2, -2);
        this.addPoint(0, 0);
        this.addPoint(2, -2);
    }

    public static Cross createCross() {
        return INSTANCE;
    }
}
