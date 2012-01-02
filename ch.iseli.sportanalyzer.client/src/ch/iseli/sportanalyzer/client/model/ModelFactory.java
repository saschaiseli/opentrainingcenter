package ch.iseli.sportanalyzer.client.model;

import java.util.Date;

import ch.iseli.sportanalyzer.client.model.impl.SimpleTraining;
import ch.opentrainingcenter.transfer.ITraining;

public class ModelFactory {
    public static ISimpleTraining createSimpleTraining(final double distanzInMeter, final double dauerInSekunden, final Date datum, final int avgHeartRate,
            final int maxHeartRate, final int maxSpeed) {
        return new SimpleTraining(distanzInMeter, dauerInSekunden, datum, avgHeartRate, maxHeartRate, maxSpeed);
    }

    public static ISimpleTraining createSimpleTraining(final ITraining overview) {
        return new SimpleTraining(overview.getLaengeInMeter(), overview.getDauerInSekunden(), overview.getDateOfStart(), overview.getAverageHeartBeat(),
                overview.getMaxHeartBeat(), overview.getMaxSpeed());
    }
}
