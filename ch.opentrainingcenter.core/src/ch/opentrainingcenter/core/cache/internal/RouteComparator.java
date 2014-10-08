package ch.opentrainingcenter.core.cache.internal;

import java.util.Comparator;

import ch.opentrainingcenter.transfer.IRoute;

public class RouteComparator implements Comparator<IRoute> {

    @Override
    public int compare(final IRoute o1, final IRoute o2) {
        return o2.getName().compareTo(o1.getName());
    }
}
