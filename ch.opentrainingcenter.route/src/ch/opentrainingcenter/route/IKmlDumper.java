package ch.opentrainingcenter.route;

import ch.opentrainingcenter.transfer.Track;

public interface IKmlDumper {

    void dumpTrack(String name, Track track);

}
