package ch.opentrainingcenter.route;

import ch.opentrainingcenter.route.impl.CompareRoute;

public final class CompareRouteFactory {

    private CompareRouteFactory() {

    }

<<<<<<< HEAD
    public static CompareRoute getRouteComparator(final boolean debug) {
        return new CompareRoute(debug);
=======
    public static CompareRoute getRouteComparator(final boolean debug, String kmlDumpPath) {
        return new CompareRoute(debug, kmlDumpPath);
>>>>>>> refs/heads/kmldumper
    }
}
