package ch.opentrainingcenter.core.cache.internal;

import java.util.Comparator;

import ch.opentrainingcenter.transfer.ITraining;

/**
 * Sortiert die Aktivitäten
 */
public class TrainingComparator implements Comparator<ITraining> {

    @Override
    public int compare(final ITraining o1, final ITraining o2) {
        return Long.valueOf(o2.getDatum()).compareTo(Long.valueOf(o1.getDatum()));
    }
}
