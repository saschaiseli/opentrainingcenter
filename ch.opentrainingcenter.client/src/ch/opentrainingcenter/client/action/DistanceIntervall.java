package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.model.training.Intervall;

public class DistanceIntervall {

    private static final int KILOMETER_IN_METER = 1000;

    private static final Logger LOGGER = Logger.getLogger(DistanceIntervall.class);

    private final Map<Intervall, List<Double>> paces = new HashMap<Intervall, List<Double>>();

    public DistanceIntervall() {
        paces.put(Intervall.KLEINER_10, new ArrayList<Double>());
        paces.put(Intervall.VON10_BIS_15, new ArrayList<Double>());
        paces.put(Intervall.VON15_BIS_20, new ArrayList<Double>());
        paces.put(Intervall.VON20_BIS_25, new ArrayList<Double>());
        paces.put(Intervall.PLUS25, new ArrayList<Double>());
    }

    public void addPace(final double pace, final double distanceInMeter) {
        final int distanceInKilometer = (int) distanceInMeter / KILOMETER_IN_METER;
        if (distanceInKilometer >= Intervall.PLUS25.getVon()) {
            paces.get(Intervall.PLUS25).add(pace);
        } else if (distanceInKilometer >= Intervall.VON20_BIS_25.getVon()) {
            paces.get(Intervall.VON20_BIS_25).add(pace);
        } else if (distanceInKilometer >= Intervall.VON15_BIS_20.getVon()) {
            paces.get(Intervall.VON15_BIS_20).add(pace);
        } else if (distanceInKilometer >= Intervall.VON10_BIS_15.getVon()) {
            paces.get(Intervall.VON10_BIS_15).add(pace);
        } else {
            paces.get(Intervall.KLEINER_10).add(pace);
        }
    }

    public double getMax(final Intervall intervall) {
        final List<Double> list = paces.get(intervall);
        if (list == null || list.size() == 0) {
            LOGGER.info("Keine pace im intervall: " + intervall); //$NON-NLS-1$
            return -1d;
        } else {
            return Collections.min(list);
        }
    }
}
