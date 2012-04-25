package ch.opentrainingcenter.client.model;

import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.client.model.impl.GpsFileModel;
import ch.opentrainingcenter.client.model.impl.GpsFileModelWrapper;
import ch.opentrainingcenter.client.model.impl.SimpleTraining;
import ch.opentrainingcenter.transfer.ITraining;

public class ModelFactory {

    /**
     * Erstellt ein SimpleTraining mit dem Lauf Typ NONE
     */
    public static ISimpleTraining createSimpleTraining(final ITraining overview) {
        return new SimpleTraining(overview.getLaengeInMeter(), overview.getDauerInSekunden(), overview.getDateOfStart(), overview.getAverageHeartBeat(),
                overview.getMaxHeartBeat(), overview.getMaxSpeed(), RunType.NONE);
    }

    public static ISimpleTraining createSimpleTraining(final double distanzInMeter, final double dauerInSekunden, final Date datum, final int avgHeartRate,
            final int maxHeartRate, final int maxSpeed, final RunType type) {
        return new SimpleTraining(distanzInMeter, dauerInSekunden, datum, avgHeartRate, maxHeartRate, maxSpeed, type);
    }

    public static ISimpleTraining createSimpleTraining(final ITraining overview, final RunType runType) {
        return new SimpleTraining(overview.getLaengeInMeter(), overview.getDauerInSekunden(), overview.getDateOfStart(), overview.getAverageHeartBeat(),
                overview.getMaxHeartBeat(), overview.getMaxSpeed(), runType);
    }

    public static IGpsFileModel createGpsFileModel(final String fileName) {
        return new GpsFileModel(fileName);
    }

    public static IGpsFileModelWrapper createGpsFileModelWrapper(final List<IGpsFileModel> fileModels) {
        return new GpsFileModelWrapper(fileModels);
    }

}
