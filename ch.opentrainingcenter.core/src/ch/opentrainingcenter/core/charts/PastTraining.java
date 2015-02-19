package ch.opentrainingcenter.core.charts;

import java.util.Collections;
import java.util.List;

import ch.opentrainingcenter.transfer.ITraining;

/**
 * Container fuer vergangene Trainings.
 */
public class PastTraining {
    private final int yearOffset;
    private final List<ITraining> trainings;

    public PastTraining(final int yearOffset, final List<ITraining> trainings) {
        this.yearOffset = yearOffset;
        this.trainings = trainings;
    }

    /**
     * @return Anzahl Jahre die zurueckgerechnet werden muessen.
     */
    public int getYearOffset() {
        return yearOffset;
    }

    public List<ITraining> getTrainings() {
        return Collections.unmodifiableList(trainings);
    }

}
