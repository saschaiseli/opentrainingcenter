package ch.opentrainingcenter.model.geo;

public class TrackPoint {
    private final float distance;
    private final float xCoordinates;
    private final float yCoordinates;

    public TrackPoint(final float distance, final float xCoordinates, final float yCoordinates) {
        this.distance = distance;
        this.xCoordinates = xCoordinates;
        this.yCoordinates = yCoordinates;
    }

    public float getDistance() {
        return distance;
    }

    public float getxCoordinates() {
        return xCoordinates;
    }

    public float getyCoordinates() {
        return yCoordinates;
    }

    @Override
    public String toString() {
        return "TrackPoint [distance=" + distance + ", xCoordinates=" + xCoordinates + ", yCoordinates=" + yCoordinates + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
}
