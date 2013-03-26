package ch.opentrainingcenter.model.geo;

public class TrackPoint {
    private final double distance;
    private final double xCoordinates;
    private final double yCoordinates;

    public TrackPoint(final double distance, final double xCoordinates, final double yCoordinates) {
        this.distance = distance;
        this.xCoordinates = xCoordinates;
        this.yCoordinates = yCoordinates;
    }

    public double getDistance() {
        return distance;
    }

    public double getxCoordinates() {
        return xCoordinates;
    }

    public double getyCoordinates() {
        return yCoordinates;
    }

    @Override
    public String toString() {
        return "TrackPoint [distance=" + distance + ", xCoordinates=" + xCoordinates + ", yCoordinates=" + yCoordinates + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
}
