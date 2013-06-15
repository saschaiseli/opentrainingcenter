package ch.opentrainingcenter.core.data;

import java.util.Comparator;

public class PairComparator<K extends Number> implements Comparator<Pair<Long, K>> {

    @Override
    public int compare(final Pair<Long, K> o1, final Pair<Long, K> o2) {
        final float a = o1.getSecond().floatValue();
        final float b = o2.getSecond().floatValue();
        final int result;
        if (a < b) {
            result = -1;
        } else if (a == b) {
            result = 0;
        } else {
            result = 1;
        }
        return result;
    }
}
