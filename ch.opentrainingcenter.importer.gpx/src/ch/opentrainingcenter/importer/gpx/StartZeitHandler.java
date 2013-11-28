package ch.opentrainingcenter.importer.gpx;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.topografix.gpx.TrksegType;
import com.topografix.gpx.WptType;

public final class StartZeitHandler {

    private static final Logger LOGGER = Logger.getLogger(StartZeitHandler.class);

    private StartZeitHandler() {

    }

    public static long getStartZeit(final TrksegType trksegType) {
        long result = 0;
        final List<WptType> trkpt = trksegType.getTrkpt();
        if (!trkpt.isEmpty()) {
            final WptType firstWayPoint = trkpt.get(0);
            if (firstWayPoint != null) {
                final GregorianCalendar greg = firstWayPoint.getTime().toGregorianCalendar();
                greg.setTimeZone(TimeZone.getDefault());
                result = greg.getTime().getTime();
            }
        }
        LOGGER.info(String.format("Startzeit des Laufes ist; %s", new Date(result))); //$NON-NLS-1$
        return result;
    }
}
