package ch.opentrainingcenter.client.views.overview;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

/**
 * Zeigt die Übersicht eines Laufes. Keinerlei Interaktionen.
 */
public class OverviewSection {

    private final ITraining training;

    public OverviewSection(final ITraining training) {
        this.training = training;
    }

    void addOverviewSection(final Composite body, final FormToolkit toolkit) {
        final Section overviewSection = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        overviewSection.setExpanded(true);
        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        overviewSection.setLayoutData(td);
        overviewSection.setText(Messages.SingleActivityViewPart1);
        overviewSection.setDescription(Messages.SingleActivityViewPart2);

        final Composite overViewComposite = toolkit.createComposite(overviewSection);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);
        final FormToolkitSupport support = new FormToolkitSupport(toolkit);
        final String readableTime = TimeHelper.convertSecondsToHumanReadableZeit(training.getDauer());
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart3, readableTime, Units.HOUR_MINUTE_SEC);
        final String laengeInKm = DistanceHelper.roundDistanceFromMeterToKm(training.getLaengeInMeter());
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart4, laengeInKm, Units.KM);
        final int avgHeartRate = training.getAverageHeartBeat();
        if (avgHeartRate > 0) {
            support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart5, String.valueOf(avgHeartRate), Units.BEATS_PER_MINUTE);
        } else {
            support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart5, "-", Units.BEATS_PER_MINUTE); //$NON-NLS-1$
        }
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart6, String.valueOf(training.getMaxHeartBeat()), Units.BEATS_PER_MINUTE);

        final String labelAverage;
        final String labelMax;
        if (Sport.RUNNING.equals(training.getSport())) {
            // running
            labelAverage = Messages.SingleActivityViewPart7;
            labelMax = Messages.SingleActivityViewPart8;
        } else {
            labelAverage = Messages.SingleActivityViewPart19;
            labelMax = Messages.SingleActivityViewPart20;
        }
        final Units unit = getUnitFuerGeschwindigkeit(training.getSport());
        final String average = DistanceHelper.calculatePace(training.getLaengeInMeter(), training.getDauer(), training.getSport());
        final String max = DistanceHelper.calculatePace(training.getMaxSpeed(), training.getSport());

        support.addLabelAndValue(overViewComposite, labelAverage, average, unit);
        support.addLabelAndValue(overViewComposite, labelMax, max, unit);
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_4, training.getUpMeter().toString(), Units.METER);
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_8, training.getDownMeter().toString(), Units.METER);
        if (training.getTrainingEffect() != null) {
            support.addLabelAndValue(overViewComposite, Messages.Common_TrainingEffect, String.valueOf(training.getTrainingEffect().doubleValue() / 10),
                    Units.NONE);
        }
        overviewSection.setClient(overViewComposite);
    }

    private Units getUnitFuerGeschwindigkeit(final Sport sport) {
        final Units unit;
        if (Sport.BIKING.equals(sport)) {
            unit = Units.GESCHWINDIGKEIT;
        } else {
            unit = Units.PACE;
        }
        return unit;
    }
}
