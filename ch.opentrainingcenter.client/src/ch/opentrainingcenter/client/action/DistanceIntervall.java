package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.core.data.PairComparator;
import ch.opentrainingcenter.model.training.Intervall;

/**
 * Definition der Distanz Kategorien.
 * 
 */
class DistanceIntervall {

    private static final int KILOMETER_IN_METER = 1000;

    private static final Logger LOGGER = Logger.getLogger(DistanceIntervall.class);

    private static final Map<Intervall, List<Pair<Long, Double>>> PACES = new HashMap<>();

    public DistanceIntervall() {
        PACES.put(Intervall.KLEINER_10, new ArrayList<Pair<Long, Double>>());
        PACES.put(Intervall.VON10_BIS_15, new ArrayList<Pair<Long, Double>>());
        PACES.put(Intervall.VON15_BIS_20, new ArrayList<Pair<Long, Double>>());
        PACES.put(Intervall.VON20_BIS_25, new ArrayList<Pair<Long, Double>>());
        PACES.put(Intervall.PLUS25, new ArrayList<Pair<Long, Double>>());
    }

    void addPace(final long date, final double pace, final double distanceInMeter) {
        final int distanceInKilometer = (int) distanceInMeter / KILOMETER_IN_METER;
        if (distanceInKilometer >= Intervall.PLUS25.getVon()) {
            PACES.get(Intervall.PLUS25).add(new Pair<Long, Double>(date, pace));
        } else if (distanceInKilometer >= Intervall.VON20_BIS_25.getVon()) {
            PACES.get(Intervall.VON20_BIS_25).add(new Pair<Long, Double>(date, pace));
        } else if (distanceInKilometer >= Intervall.VON15_BIS_20.getVon()) {
            PACES.get(Intervall.VON15_BIS_20).add(new Pair<Long, Double>(date, pace));
        } else if (distanceInKilometer >= Intervall.VON10_BIS_15.getVon()) {
            PACES.get(Intervall.VON10_BIS_15).add(new Pair<Long, Double>(date, pace));
        } else {
            PACES.get(Intervall.KLEINER_10).add(new Pair<Long, Double>(date, pace));
        }
    }

    Pair<Long, Double> getMax(final Intervall intervall) {
        final List<Pair<Long, Double>> list = PACES.get(intervall);
        if (list == null || list.isEmpty()) {
            LOGGER.info("Keine pace im intervall: " + intervall); //$NON-NLS-1$
            return new Pair<Long, Double>(null, null);
        } else {
            return Collections.min(list, new PairComparator<Double>());
        }
    }
}
