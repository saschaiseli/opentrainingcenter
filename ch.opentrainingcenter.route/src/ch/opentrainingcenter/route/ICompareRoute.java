package ch.opentrainingcenter.route;

import ch.opentrainingcenter.model.geo.Track;

public interface ICompareRoute {

    boolean compareRoute(Track reference, Track track);
}
