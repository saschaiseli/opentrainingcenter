package ch.opentrainingcenter.core.cache.internal;

import java.util.Comparator;
import java.util.Date;

/**
 * Sortiert die Aktivitäten
 */
public class ImportedComparator implements Comparator<Date> {

    @Override
    public int compare(final Date o1, final Date o2) {
        return o2.compareTo(o1);
    }
}
