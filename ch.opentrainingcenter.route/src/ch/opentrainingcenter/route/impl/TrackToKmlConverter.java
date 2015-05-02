package ch.opentrainingcenter.route.impl;

import java.util.List;

import ch.opentrainingcenter.core.data.SimplePair;
import ch.opentrainingcenter.route.ITrackToKmlConverter;
import ch.opentrainingcenter.route.kml.KmlFile;
import ch.opentrainingcenter.transfer.Track;

public class TrackToKmlConverter implements ITrackToKmlConverter {

    @Override
    public String toKmlString(final String fileName, final String label, final Track track, final List<SimplePair<String>> extendedData) {
        final KmlFile kmlFile = new KmlFile(fileName);
        kmlFile.addKmlLine(fileName, "ff0000ff", track.toKml()); //$NON-NLS-1$
        kmlFile.addPlacemark(fileName, extendedData, getRandomTrackPoint(track));
        return kmlFile.toKmlString();
    }

    private String getRandomTrackPoint(final Track track) {
        final int size = track.getPoints().size() - 1;
        final int index = 0 + (int) (Math.random() * size);
        if (index >= size) {
            System.err.println("??"); //$NON-NLS-1$
        }
        return track.getPoints().get(index).toKml();
    }
}
