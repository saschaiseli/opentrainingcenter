package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.SpeedCalculator;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.model.training.Intervall;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public class GoldMedalAction {
    private static final String UNKNOWN = "-"; //$NON-NLS-1$

    public IGoldMedalModel getModel(final List<IImported> allImported) {
        final IGoldMedalModel result = ModelFactory.createGoldMedalModel();
        final List<Double> maxSpeed = new ArrayList<Double>();
        final List<Double> laenge = new ArrayList<Double>();
        final List<Double> dauer = new ArrayList<Double>();
        final List<Integer> heart = new ArrayList<Integer>();
        final List<Integer> averageHeart = new ArrayList<Integer>();
        final DistanceIntervall di = new DistanceIntervall();
        for (final IImported imported : allImported) {
            final ITraining training = imported.getTraining();
            final double pace = SpeedCalculator.calculatePace(0, training.getLaengeInMeter(), 0, training.getDauerInSekunden());
            maxSpeed.add(pace);
            di.addPace(pace, training.getLaengeInMeter());
            laenge.add(training.getLaengeInMeter());
            dauer.add(training.getDauerInSekunden());
            heart.add(training.getMaxHeartBeat());
            if (training.getAverageHeartBeat() > 0) {
                averageHeart.add(training.getAverageHeartBeat());
            }
        }

        result.setSchnellstePace(calculateBestePace(maxSpeed));
        result.setLongestDistance(!laenge.isEmpty() ? Double.parseDouble(DistanceHelper.roundDistanceFromMeterToKm(Collections.max(laenge))) : -1);
        result.setLongestRun(!dauer.isEmpty() ? TimeHelper.convertSecondsToHumanReadableZeit(Collections.max(dauer)) : UNKNOWN);
        result.setHighestPulse(!heart.isEmpty() ? Collections.max(heart) : -1);
        result.setHighestAveragePulse(!averageHeart.isEmpty() ? Collections.max(averageHeart) : -1);
        result.setLowestAveragePulse(!averageHeart.isEmpty() ? Collections.min(averageHeart) : -1);

        result.setSchnellstePace(Intervall.KLEINER_10, getPace(di, Intervall.KLEINER_10));
        result.setSchnellstePace(Intervall.VON10_BIS_15, getPace(di, Intervall.VON10_BIS_15));
        result.setSchnellstePace(Intervall.VON15_BIS_20, getPace(di, Intervall.VON15_BIS_20));
        result.setSchnellstePace(Intervall.VON20_BIS_25, getPace(di, Intervall.VON20_BIS_25));
        result.setSchnellstePace(Intervall.PLUS25, getPace(di, Intervall.PLUS25));

        return result;
    }

    private String getPace(final DistanceIntervall di, final Intervall intervall) {
        return String.valueOf(di.getMax(intervall) > 0 ? di.getMax(intervall) : UNKNOWN);
    }

    private String calculateBestePace(final List<Double> maxSpeed) {
        String result = UNKNOWN;
        if (!maxSpeed.isEmpty()) {
            final Double min = Collections.min(maxSpeed);
            if (min != null && min > 0) {
                result = String.valueOf(min);
            }
        }
        return result;
    }
}
