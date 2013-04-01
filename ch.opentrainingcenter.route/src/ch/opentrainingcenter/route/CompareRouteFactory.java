package ch.opentrainingcenter.route;

import ch.opentrainingcenter.route.impl.CompareRoute;

public final class CompareRouteFactory {

    private CompareRouteFactory() {

    }

    public static CompareRoute getRouteComparator(final boolean debug, final String kmlDumpPath) {
        return new CompareRoute(debug, kmlDumpPath);

    }
}
