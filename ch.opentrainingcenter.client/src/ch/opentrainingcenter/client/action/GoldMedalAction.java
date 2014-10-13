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
import ch.opentrainingcenter.model.training.GoldMedalTyp;
import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.model.training.Intervall;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

/**
 * Sucht die besten Resultate in den verschiedenen Kategorien
 * {@link DistanceIntervall}
 * 
 */
public class GoldMedalAction {
    private static final String UNKNOWN = "-"; //$NON-NLS-1$
    private final Sport sport;

    public GoldMedalAction(final Sport sport) {
        this.sport = sport;
    }

    public IGoldMedalModel getModel(final List<ITraining> trainings) {
        // final List<ITraining> relevante = trainings.stream().filter(element
        // -> sport.equals(element.getSport())).collect(Collectors.toList());
        final List<ITraining> relevante = new ArrayList<>();
        for (final ITraining training : trainings) {
            if (sport.equals(training.getSport())) {
                relevante.add(training);
            }
        }
        final IGoldMedalModel result = ModelFactory.createGoldMedalModel();
        if (relevante == null || relevante.isEmpty()) {
            return result;
        }
        final List<Pair<Long, Double>> maxSpeed = new ArrayList<>();
        final List<Pair<Long, Double>> laenge = new ArrayList<>();
        final List<Pair<Long, Double>> dauer = new ArrayList<>();
        final List<Pair<Long, Integer>> heart = new ArrayList<>();
        final List<Pair<Long, Integer>> averageHeart = new ArrayList<>();

        final DistanceIntervall di = new DistanceIntervall();
        for (final ITraining training : relevante) {
            final long datum = training.getDatum();
            // final double pace = SpeedCalculator.calculatePace(0,
            // training.getLaengeInMeter(), 0, training.getDauer());
            final double mps = SpeedCalculator.calculateSpeedMpS(0, training.getLaengeInMeter(), 0, training.getDauer());
            maxSpeed.add(new Pair<Long, Double>(datum, mps));
            di.addPace(datum, mps, training.getLaengeInMeter());
            laenge.add(new Pair<Long, Double>(datum, training.getLaengeInMeter()));
            dauer.add(new Pair<Long, Double>(datum, training.getDauer()));
            heart.add(new Pair<Long, Integer>(datum, training.getMaxHeartBeat()));
            if (training.getAverageHeartBeat() > 0) {
                averageHeart.add(new Pair<Long, Integer>(datum, training.getAverageHeartBeat()));
            }
        }
        final Pair<Long, String> emptyPair = IGoldMedalModel.EMPTY_PAIR;

        result.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, calculateBestePace(maxSpeed));
        // längster lauf
        final Pair<Long, Double> max = Collections.max(laenge, new PairComparator<Double>());
        if (max.getSecond() != null && max.getSecond().doubleValue() > 0) {
            result.setRecord(GoldMedalTyp.LAENGSTE_DISTANZ, new Pair<Long, String>(max.getFirst(), DistanceHelper.roundDistanceFromMeterToKm(max.getSecond())));
        } else {
            result.setRecord(GoldMedalTyp.LAENGSTE_DISTANZ, emptyPair);
        }
        // längster lauf zeit
        final Pair<Long, Double> longRun = Collections.max(dauer, new PairComparator<Double>());
        final String seconds = TimeHelper.convertSecondsToHumanReadableZeit(longRun.getSecond().longValue());
        if (dauer.isEmpty()) {
            result.setRecord(GoldMedalTyp.LAENGSTER_LAUF, emptyPair);
        } else {
            result.setRecord(GoldMedalTyp.LAENGSTER_LAUF, new Pair<Long, String>(longRun.getFirst(), seconds));
        }

        // höchster puls
        if (!heart.isEmpty()) {
            final Pair<Long, Integer> highPuls = Collections.max(heart, new PairComparator<Integer>());
            if (highPuls.getSecond() != null && highPuls.getSecond().intValue() > 0) {
                result.setRecord(GoldMedalTyp.HOECHSTER_PULS, new Pair<Long, String>(highPuls.getFirst(), String.valueOf(highPuls.getSecond())));
            }
        } else {
            result.setRecord(GoldMedalTyp.HOECHSTER_PULS, emptyPair);
        }
        // durchschnittlicher puls
        if (!averageHeart.isEmpty()) {
            final Pair<Long, Integer> avgHeart = Collections.max(averageHeart, new PairComparator<Integer>());
            if (avgHeart.getSecond() != null && avgHeart.getSecond().intValue() > 0) {
                result.setRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS, new Pair<Long, String>(avgHeart.getFirst(), avgHeart.getSecond().toString()));
            }

            final Pair<Long, Integer> lowestPuls = Collections.min(averageHeart, new PairComparator<Integer>());
            if (lowestPuls.getSecond() != null && lowestPuls.getSecond().intValue() > 0) {
                result.setRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS, new Pair<Long, String>(lowestPuls.getFirst(), lowestPuls.getSecond().toString()));
            }
        } else {
            result.setRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS, emptyPair);
            result.setRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS, emptyPair);
        }

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
        final Double speedMperSecond = max.getSecond();
        if (speedMperSecond != null) {
            final String pace = DistanceHelper.calculatePace(speedMperSecond, sport);
            result = new Pair<Long, String>(max.getFirst(), pace);
        } else {
            result = new Pair<Long, String>();
        }
        return result;
    }

    private Pair<Long, String> calculateBestePace(final List<Pair<Long, Double>> maxSpeed) {
        Pair<Long, String> result = new Pair<Long, String>(null, UNKNOWN);
        if (!maxSpeed.isEmpty()) {
            final Pair<Long, Double> maxMps = Collections.max(maxSpeed, new PairComparator<Double>());
            if (maxMps != null && maxMps.getSecond().doubleValue() > 0) {
                final String pace = DistanceHelper.calculatePace(maxMps.getSecond(), sport);
                result = new Pair<Long, String>(maxMps.getFirst(), pace);
            }
        }
        return result;
    }

}
