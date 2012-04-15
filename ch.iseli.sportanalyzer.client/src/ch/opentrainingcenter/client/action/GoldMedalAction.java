package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.opentrainingcenter.client.helper.DistanceHelper;
import ch.opentrainingcenter.client.helper.SpeedCalculator;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.model.IGoldMedalModel;
import ch.opentrainingcenter.client.model.impl.GoldMedalModel;
import ch.opentrainingcenter.client.model.impl.GoldMedalModel.Intervall;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public class GoldMedalAction {
    private static final String UNKNOWN = "-"; //$NON-NLS-1$

    public IGoldMedalModel getModel(final List<IImported> allImported) {
        final IGoldMedalModel result = new GoldMedalModel();
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
            averageHeart.add(training.getAverageHeartBeat());
        }
        result.setSchnellstePace(String.valueOf(Collections.min(maxSpeed)));

        final String paceKl10 = String.valueOf(di.getMax(Intervall.KLEINER_10) > 0 ? di.getMax(Intervall.KLEINER_10) : UNKNOWN);
        result.setSchnellstePace(Intervall.KLEINER_10, paceKl10);
        final String pace10 = String.valueOf(di.getMax(Intervall.VON10_BIS_15) > 0 ? di.getMax(Intervall.VON10_BIS_15) : UNKNOWN);
        result.setSchnellstePace(Intervall.VON10_BIS_15, pace10);
        final String pace15 = String.valueOf(di.getMax(Intervall.VON15_BIS_20) > 0 ? di.getMax(Intervall.VON15_BIS_20) : UNKNOWN);
        result.setSchnellstePace(Intervall.VON15_BIS_20, pace15);
        final String pace20 = String.valueOf(di.getMax(Intervall.VON20_BIS_25) > 0 ? di.getMax(Intervall.VON20_BIS_25) : UNKNOWN);
        result.setSchnellstePace(Intervall.VON20_BIS_25, pace20);
        final String pace25 = String.valueOf(di.getMax(Intervall.PLUS25) > 0 ? di.getMax(Intervall.PLUS25) : UNKNOWN);
        result.setSchnellstePace(Intervall.PLUS25, pace25);

        result.setLongestDistance(Double.valueOf(DistanceHelper.roundDistanceFromMeterToKm(Collections.max(laenge))));
        result.setLongestRun(TimeHelper.convertSecondsToHumanReadableZeit(Collections.max(dauer)));
        result.setHighestPulse(Collections.max(heart));
        result.setHighestAveragePulse(Collections.max(averageHeart));
        return result;
    }
}
