package ch.opentrainingcenter.charts.bar.internal;

import java.util.ArrayList;
import java.util.List;

public class SpeedCompressor {

    private final int bundleSize;

    public SpeedCompressor(final int bundleSize) {
        this.bundleSize = bundleSize;

    }

    /**
     * Reduziert die Datenmenge und macht Datenbündel mit der im Konstruktor
     * angegeben Grösse. Der Wert ist dann einfach der Durchschnitt der Werte.
     */
    public List<PositionPace> compressSpeedDataPoints(final List<PositionPace> positionPaces) {
        final List<PositionPace> result = new ArrayList<PositionPace>();
        if (isValid(positionPaces)) {
            int counter = 0;
            final List<PositionPace> compressedPositionPace = new ArrayList<PositionPace>();
            for (final PositionPace positionPace : positionPaces) {
                if (counter >= bundleSize) {
                    result.add(berechneDurchschnitt(compressedPositionPace));
                    compressedPositionPace.clear();
                    counter = 0;
                }
                compressedPositionPace.add(positionPace);
                counter++;
            }
            result.add(berechneDurchschnitt(compressedPositionPace));
        }
        return result;
    }

    private PositionPace berechneDurchschnitt(final List<PositionPace> compressedPositionPace) {
        double pacesSum = 0;
        for (final PositionPace positionPace : compressedPositionPace) {
            pacesSum += positionPace.getPace();
        }
        final double avg = pacesSum / compressedPositionPace.size();
        final double position = compressedPositionPace.get(compressedPositionPace.size() - 1).getPosition();
        return new PositionPace(position, avg);
    }

    private boolean isValid(final List<PositionPace> positionPaces) {
        return positionPaces != null && !positionPaces.isEmpty();
    }
}
