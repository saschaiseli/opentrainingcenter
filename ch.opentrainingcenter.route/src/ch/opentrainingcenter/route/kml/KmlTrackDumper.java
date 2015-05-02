package ch.opentrainingcenter.route.kml;

import java.util.List;

import ch.opentrainingcenter.core.data.SimplePair;
import ch.opentrainingcenter.route.IKmlDumper;
import ch.opentrainingcenter.route.impl.TrackToKmlConverter;
import ch.opentrainingcenter.transfer.Track;

public class KmlTrackDumper implements IKmlDumper {

    private final String kmlPath;
    private final TrackToKmlConverter converter;

    public KmlTrackDumper(final String kmlPath) {
        this.kmlPath = kmlPath;
        converter = new TrackToKmlConverter();
    }

    @Override
    public void dumpTrack(final String fileName, final String label, final Track track, final List<SimplePair<String>> extendedData) {
        final KmlDumper kmlDumper = new KmlDumper(fileName, kmlPath);
        kmlDumper.dump(converter.toKmlString(fileName, label, track, extendedData));
    }

    @Override
    public String getKmlPath() {
        return kmlPath;
    }

}
