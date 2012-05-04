package ch.opentrainingcenter.client.cache.impl;

import java.util.Comparator;
import java.util.Date;

/**
 * Sortiert die Aktivitäten
 */
class ImportedComparator implements Comparator<Date> {

    @Override
    public int compare(final Date o1, final Date o2) {
        return o2.compareTo(o1);
    }
}
