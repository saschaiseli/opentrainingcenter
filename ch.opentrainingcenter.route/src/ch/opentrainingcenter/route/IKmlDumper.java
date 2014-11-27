package ch.opentrainingcenter.route;

import java.util.List;

import ch.opentrainingcenter.core.data.SimplePair;
import ch.opentrainingcenter.transfer.Track;

public interface IKmlDumper {

    void dumpTrack(String fileName, String label, Track track, List<SimplePair<String>> extendedData);

}
