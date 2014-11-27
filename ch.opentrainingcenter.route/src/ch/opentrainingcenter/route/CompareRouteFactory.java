package ch.opentrainingcenter.route;

import ch.opentrainingcenter.route.impl.CompareRoute;
import ch.opentrainingcenter.route.kml.KmlTrackDumper;

public final class CompareRouteFactory {

    private CompareRouteFactory() {

    }

    public static IKmlDumper createKmlDumper(final String kmlPath) {
        return new KmlTrackDumper(kmlPath);
    }

    public static ICompareRoute getRouteComparator(final boolean debug, final String kmlDumpPath) {
        return new CompareRoute(debug, kmlDumpPath);
    }
}
