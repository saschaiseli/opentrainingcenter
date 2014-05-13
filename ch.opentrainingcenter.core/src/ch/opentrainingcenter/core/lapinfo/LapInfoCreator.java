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
    public LapInfoCreator(double distance) {
        Assertions.isValid(distance <= 0, "Distanz muss positiv sein");
        this.distance = distance;
    }

    /**
     * Splittet das Training in Laps auf. Die Distanz einer Lap wird im
     * Konstruktor definiert.
     * 
     * @param training
     * @return
     */
    public List<ILapInfo> createLapInfos(ITraining training) {
        List<ILapInfo> result = new ArrayList<>();
        if (training.getTrackPoints().isEmpty()) {
            return result;
        }
        NavigableMap<Integer, List<ITrackPointProperty>> points = createLapMap(training);
        Integer lastLap = points.lastKey();
        ITrackPointProperty previous = null;
        for (int i = 0; i <= lastLap.intValue(); i++) {
            List<ITrackPointProperty> pointsOfLap = points.get(i);
            if (pointsOfLap == null) {
                result.add(createEmptyLap());
            } else {
                result.add(createLapInfo(pointsOfLap, previous));
                previous = pointsOfLap.get(pointsOfLap.size() - 1);
            }
        }
        return result;
    }

    private ILapInfo createEmptyLap() {
        return CommonTransferFactory.createLapInfo(0, 0, 0, 0, "--:--");
    }

    private NavigableMap<Integer, List<ITrackPointProperty>> createLapMap(ITraining training) {
        NavigableMap<Integer, List<ITrackPointProperty>> points = new TreeMap<>();
        for (ITrackPointProperty point : training.getTrackPoints()) {
            int lap = getLap(point.getDistance());
            if (!points.containsKey(lap)) {
                points.put(lap, new ArrayList<ITrackPointProperty>());
            }
            points.get(lap).add(point);
        }
        return points;
    }

    private int getLap(double pointDistance) {
        int mod = (int) (pointDistance / distance);
        return mod;
    }

    private ILapInfo createLapInfo(List<ITrackPointProperty> points, ITrackPointProperty previous) {
        final ILapInfo result;
        double initPosition = 0;
        long initTime = 0;
        if (previous != null) {
            initPosition = previous.getDistance();
            initTime = previous.getZeit();
        }
        result = LapInfoSupport.createLapInfo(points, initPosition, initTime);
        return result;
    }
}
