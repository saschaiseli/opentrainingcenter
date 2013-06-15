package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.core.data.PairComparator;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.SpeedCalculator;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.model.training.Intervall;
import ch.opentrainingcenter.transfer.ITraining;

public class GoldMedalAction {
    private static final String UNKNOWN = "-"; //$NON-NLS-1$

    public IGoldMedalModel getModel(final List<ITraining> trainings) {
        final IGoldMedalModel result = ModelFactory.createGoldMedalModel();
        final List<Pair<Long, Double>> maxSpeed = new ArrayList<>();
        final List<Pair<Long, Double>> laenge = new ArrayList<>();
        final List<Pair<Long, Double>> dauer = new ArrayList<>();
        final List<Pair<Long, Integer>> heart = new ArrayList<>();
        final List<Pair<Long, Integer>> averageHeart = new ArrayList<>();

        final DistanceIntervall di = new DistanceIntervall();
        for (final ITraining training : trainings) {
            final long datum = training.getDatum();
            final double pace = SpeedCalculator.calculatePace(0, training.getLaengeInMeter(), 0, training.getDauer());
            maxSpeed.add(new Pair<Long, Double>(datum, pace));
            di.addPace(datum, pace, training.getLaengeInMeter());
            laenge.add(new Pair<Long, Double>(datum, training.getLaengeInMeter()));
            dauer.add(new Pair<Long, Double>(datum, training.getDauer()));
            heart.add(new Pair<Long, Integer>(datum, training.getMaxHeartBeat()));
            if (training.getAverageHeartBeat() > 0) {
                averageHeart.add(new Pair<Long, Integer>(datum, training.getAverageHeartBeat()));
            }
        }
        final Pair<Long, String> emptyPair = result.getEmpty();

        result.setSchnellstePace(calculateBestePace(maxSpeed));
        // längster lauf
        final Pair<Long, Double> max = Collections.max(laenge, new PairComparator<Double>());
        result.setLongestDistance(new Pair<Long, String>(max.getFirst(), DistanceHelper.roundDistanceFromMeterToKm(max.getSecond())));
        // längster lauf zeit
        final Pair<Long, Double> longRun = Collections.max(dauer, new PairComparator<Double>());
        final String seconds = TimeHelper.convertSecondsToHumanReadableZeit(longRun.getSecond());
        result.setLongestRun(!dauer.isEmpty() ? new Pair<Long, String>(longRun.getFirst(), seconds) : emptyPair);
        // höchster puls
        final Pair<Long, Integer> highPuls = Collections.max(heart, new PairComparator<Integer>());
        result.setHighestPulse(!heart.isEmpty() ? new Pair<Long, String>(highPuls.getFirst(), String.valueOf(highPuls.getSecond())) : emptyPair);
        // durchschnittlicher puls
        final Pair<Long, Integer> avgHeart = Collections.max(averageHeart, new PairComparator<Integer>());
        result.setHighestAveragePulse(!averageHeart.isEmpty() ? new Pair<Long, String>(avgHeart.getFirst(), avgHeart.getSecond().toString()) : emptyPair);

        final Pair<Long, Integer> lowestPuls = Collections.min(averageHeart, new PairComparator<Integer>());
        result.setLowestAveragePulse(!averageHeart.isEmpty() ? new Pair<Long, String>(lowestPuls.getFirst(), lowestPuls.getSecond().toString()) : emptyPair);

        result.setSchnellstePace(Intervall.KLEINER_10, getPace(di, Intervall.KLEINER_10));
        result.setSchnellstePace(Intervall.VON10_BIS_15, getPace(di, Intervall.VON10_BIS_15));
        result.setSchnellstePace(Intervall.VON15_BIS_20, getPace(di, Intervall.VON15_BIS_20));
        result.setSchnellstePace(Intervall.VON20_BIS_25, getPace(di, Intervall.VON20_BIS_25));
        result.setSchnellstePace(Intervall.PLUS25, getPace(di, Intervall.PLUS25));

        return result;
    }

    private Pair<Long, String> getPace(final DistanceIntervall di, final Intervall intervall) {
        final Pair<Long, String> result;
        final Pair<Long, Double> max = di.getMax(intervall);
        if (max.getSecond() != null) {
            result = new Pair<Long, String>(max.getFirst(), max.getSecond().toString());
        } else {
            result = new Pair<Long, String>();
        }
        return result;
    }

    private Pair<Long, String> calculateBestePace(final List<Pair<Long, Double>> maxSpeed) {
        Pair<Long, String> result = null;
        if (!maxSpeed.isEmpty()) {
            final Pair<Long, Double> min = Collections.min(maxSpeed, new PairComparator<Double>());
            if (min != null && min.getSecond().doubleValue() > 0) {
                result = new Pair<Long, String>(min.getFirst(), String.valueOf(min.getSecond()));
            }
        }
        if (result == null) {
            result = new Pair<Long, String>(null, UNKNOWN);
        }
        return result;
    }

}
