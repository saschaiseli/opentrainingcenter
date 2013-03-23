package ch.opentrainingcenter.route.impl;

import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.route.ICompareRoute;

public class CompareRoute implements ICompareRoute {

    @Override
    public boolean compareRoute(final Track reference, final Track track) {
        return false;
    }

}
