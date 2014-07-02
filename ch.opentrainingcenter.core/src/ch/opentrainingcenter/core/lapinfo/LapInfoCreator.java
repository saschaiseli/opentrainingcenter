package ch.opentrainingcenter.core.lapinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class LapInfoCreator {
    private final double distance;

    /**
     * Constructor.
     * 
     * @param distance
     *            Distanz in Meter [m]
     */
    public LapInfoCreator(final double distance) {
        Assertions.isValid(distance <= 0, "Distanz muss positiv sein"); //$NON-NLS-1$
        this.distance = distance;
    }

    /**
     * Splittet das Training in Laps auf. Die Distanz einer Lap wird im
     * Konstruktor definiert.
     * 
     * @param training
     * @return eine Liste mit {@link ILapInfo}s.
     */
    public List<ILapInfo> createLapInfos(final ITraining training) {
        final List<ILapInfo> result = new ArrayList<>();
        if (training.getTrackPoints().isEmpty()) {
            return result;
        }
        final NavigableMap<Integer, List<ITrackPointProperty>> points = createLapMap(training);
        final Integer lastLap = points.lastKey();
        ITrackPointProperty previous = null;
        for (int i = 0; i <= lastLap.intValue(); i++) {
            final List<ITrackPointProperty> pointsOfLap = points.get(i);
            if (pointsOfLap == null) {
                result.add(createEmptyLap());
            } else {
                result.add(createLapInfo(i, pointsOfLap, previous));
                previous = pointsOfLap.get(pointsOfLap.size() - 1);
            }
        }
        return result;
    }

    private ILapInfo createEmptyLap() {
        return CommonTransferFactory.createLapInfo(0, 0, 0, 0, 0, "--:--"); //$NON-NLS-1$
    }

    private NavigableMap<Integer, List<ITrackPointProperty>> createLapMap(final ITraining training) {
        final NavigableMap<Integer, List<ITrackPointProperty>> points = new TreeMap<>();
        for (final ITrackPointProperty point : training.getTrackPoints()) {
            final int lap = getLap(point.getDistance());
            if (!points.containsKey(lap)) {
                points.put(lap, new ArrayList<ITrackPointProperty>());
            }
            points.get(lap).add(point);
        }
        return points;
    }

    private int getLap(final double pointDistance) {
        return (int) (pointDistance / distance);
    }

    private ILapInfo createLapInfo(final int runde, final List<ITrackPointProperty> points, final ITrackPointProperty previous) {
        final ILapInfo result;
        double initPosition = 0;
        long initTime = 0;
        if (previous != null) {
            initPosition = previous.getDistance();
            initTime = previous.getZeit();
        }
        result = LapInfoSupport.createLapInfo(runde, points, initPosition, initTime);
        return result;
    }
}
