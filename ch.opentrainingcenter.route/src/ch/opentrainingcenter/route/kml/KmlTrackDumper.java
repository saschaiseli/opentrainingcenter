package ch.opentrainingcenter.route.kml;

import java.util.List;

import ch.opentrainingcenter.core.data.SimplePair;
import ch.opentrainingcenter.route.IKmlDumper;
import ch.opentrainingcenter.transfer.Track;

public class KmlTrackDumper implements IKmlDumper {

    private final String kmlPath;

    public KmlTrackDumper(final String kmlPath) {
        this.kmlPath = kmlPath;
    }

    @Override
    public void dumpTrack(final String fileName, final String label, final Track track, final List<SimplePair<String>> extendedData) {
        final KmlDumper kmlDumper = new KmlDumper(fileName, kmlPath);
        kmlDumper.addLine(fileName, "ff0000ff", track.toKml()); //$NON-NLS-1$
        kmlDumper.addPlacemark(label, extendedData, getRandomTrackPoint(track));
        kmlDumper.dump();
    }

    private String getRandomTrackPoint(final Track track) {
        final int size = track.getPoints().size();
        final int index = 0 + (int) (Math.random() * size);
        return track.getPoints().get(index).toKml();
    }
}
