package ch.opentrainingcenter.client.action.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.joda.time.Interval;

import ch.opentrainingcenter.core.sort.TrainingComparator;
import ch.opentrainingcenter.model.summary.SummaryModel;
import ch.opentrainingcenter.model.summary.SummaryModel.SummaryBuilder;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

/**
 * Berechnet die Zusammenfassung von n Trainings
 */
public class SummaryAction {

    private final List<ITraining> allTrainings;

    public SummaryAction(final List<ITraining> allTrainings) {
        this.allTrainings = allTrainings;// filter(allTrainings, sport);
        Collections.sort(this.allTrainings, new TrainingComparator());
    }

    private List<ITraining> filter(final List<ITraining> trainings, final Sport sport) {
        final List<ITraining> result = new ArrayList<>();
        for (final ITraining training : trainings) {
            if (sport.equals(training.getSport())) {
                result.add(training);
            }
        }
        return result;
    }

    /**
     * @return die Zusammenfassung der Trainings
     */
    SummaryModel calculateSummary(final Sport sport) {
        final List<ITraining> trainings = filter(allTrainings, sport);
        final int anzahl = trainings.size();
        final SummaryBuilder builder = new SummaryBuilder();
        if (anzahl > 0) {
            final float distanzInMeter = calculateMinMax(builder, anzahl, trainings);
            calculateProInterval(builder, anzahl, distanzInMeter, trainings);
        }
        return builder.build();
    }

    private float calculateMinMax(final SummaryBuilder builder, final int anzahl, final List<ITraining> trainings) {
        final Collection<Integer> heartsMax = new ArrayList<>();
        int heartAvg = 0;
        long dauer = 0;
        float distanzInMeter = 0;
        for (final ITraining training : trainings) {
            distanzInMeter += training.getLaengeInMeter();
            heartsMax.add(training.getMaxHeartBeat());
            heartAvg += training.getAverageHeartBeat();
            dauer += training.getDauer();
        }
        builder.distance(distanzInMeter).dauer(dauer);

        builder.maxHeart(Collections.max(heartsMax)).avgHeart(heartAvg / anzahl);
        return distanzInMeter;
    }

    private void calculateProInterval(final SummaryBuilder builder, final int anzahl, final float distanzInMeter, final List<ITraining> trainings) {
        final Interval startEnd = new Interval(trainings.get(anzahl - 1).getDatum(), trainings.get(0).getDatum());
        builder.interval(startEnd);
        final int days = (int) startEnd.toDuration().getStandardDays();
        int wochen = days / 7;
        if (days % 7 > 0 || days == 0) {
            wochen += 1;
        }
        int monate = days / 31;
        if (days % 31 > 0 || days == 0) {
            monate += 1;
        }
        final int distanzInKm = (int) distanzInMeter / 1000;
        builder.kmPerWeek((float) distanzInKm / wochen).kmPerMonth((float) distanzInKm / monate);
        builder.trainingPerWeek((float) anzahl / wochen).trainingPerMonth((float) anzahl / monate);
    }
}
