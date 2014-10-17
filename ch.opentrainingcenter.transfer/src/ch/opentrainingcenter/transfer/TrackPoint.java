package ch.opentrainingcenter.transfer;

public class TrackPoint {
    private final double distance;
    private final double xCoordinates;
    private final double yCoordinates;

    /**
     * @param distance
     *            Distance in meter
     * @param xCoordinates
     *            latitude
     * @param yCoordinates
     *            longitude
     */
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

    /**
     * @return die koordinaten in form von 'yCoord, xCoord '
     */
    public String toKml() {
        return yCoordinates + "," + xCoordinates + " "; //$NON-NLS-1$//$NON-NLS-2$
    }

    @Override
    public String toString() {
        return "TrackPoint [gps=" + yCoordinates + "," + xCoordinates + " distance=" + distance + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
}
