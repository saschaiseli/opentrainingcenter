package ch.opentrainingcenter.client.charts;

import java.awt.Polygon;

public final class Cross extends Polygon {
    private static final int NEGATIV = -2;

    private static final int CENTER = 0;

    private static final int POSITIV = 2;

    private static final long serialVersionUID = 1L;

    private static final Cross INSTANCE = new Cross();

    private Cross() {
        this.addPoint(CENTER, CENTER);
        this.addPoint(POSITIV, POSITIV);
        this.addPoint(CENTER, CENTER);
        this.addPoint(NEGATIV, POSITIV);
        this.addPoint(CENTER, CENTER);
        this.addPoint(NEGATIV, NEGATIV);
        this.addPoint(CENTER, CENTER);
        this.addPoint(POSITIV, NEGATIV);
    }

    public static Cross createCross() {
        return INSTANCE;
    }
}
