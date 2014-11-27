package ch.opentrainingcenter.route.kml;

import ch.opentrainingcenter.route.IKmlDumper;
import ch.opentrainingcenter.transfer.Track;

public class KmlTrackDumper implements IKmlDumper {

    private final String kmlPath;

    public KmlTrackDumper(final String kmlPath) {
        this.kmlPath = kmlPath;

    }

    @Override
    public void dumpTrack(final String fileName, final Track track) {
        final KmlDumper kmlDumper = new KmlDumper(fileName, kmlPath);
        kmlDumper.addLine(fileName, "ff0000ff", track.toKml()); //$NON-NLS-1$
        kmlDumper.dump();
    }
}
