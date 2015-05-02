package ch.opentrainingcenter.route;

import java.util.List;

import ch.opentrainingcenter.core.data.SimplePair;
import ch.opentrainingcenter.transfer.Track;

public interface ITrackToKmlConverter {
    /**
     * Erstellt einen KML String aus einem Track.
     */
    String toKmlString(final String fileName, final String label, final Track track, final List<SimplePair<String>> extendedData);
}
