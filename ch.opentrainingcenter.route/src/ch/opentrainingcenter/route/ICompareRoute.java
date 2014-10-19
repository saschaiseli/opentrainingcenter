package ch.opentrainingcenter.route;

import ch.opentrainingcenter.transfer.Track;

public interface ICompareRoute {

    boolean compareRoute(Track reference, Track track);

}
