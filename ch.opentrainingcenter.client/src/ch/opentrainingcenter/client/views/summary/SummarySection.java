package ch.opentrainingcenter.client.views.summary;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.summary.SummaryModel;
import ch.opentrainingcenter.transfer.Sport;

public class SummarySection {
    private static final Logger LOGGER = Logger.getLogger(SummarySection.class);
    private static final String EMPTY = ""; //$NON-NLS-1$
    private final FormToolkit toolkit;
    private final FormToolkitSupport support;
    private Section section;
    private Label distanz;

    private Label dauer;

    private Label maxHeart;

    private Label avgHeart;

    private Label pace;

    private Label trainingPerWeek;

    private Label trainingPerMonth;

    private Label kmPerWeek;

    private Label kmPerMonth;
    private SummaryModel model;

    public SummarySection(final FormToolkit toolkit) {
        this.toolkit = toolkit;
        support = new FormToolkitSupport(this.toolkit);
    }

    void addSection(final Composite body, final Sport sport) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                section = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
                section.setText(sport.getTranslated() + ": " + Messages.SummaryView_2); //$NON-NLS-1$
                GridLayoutFactory.swtDefaults().numColumns(1).applyTo(section);
                GridDataFactory.swtDefaults().grab(false, false).align(SWT.BEGINNING, SWT.BEGINNING).applyTo(section);

                final Composite composite = toolkit.createComposite(section);
                GridLayoutFactory.swtDefaults().numColumns(3).applyTo(composite);
                GridDataFactory.swtDefaults().grab(false, false).align(SWT.BEGINNING, SWT.BEGINNING).applyTo(composite);

                distanz = support.addLabelAndValue(composite, Messages.SummaryView_3, EMPTY, Units.KM);
                dauer = support.addLabelAndValue(composite, Messages.SummaryView_4, EMPTY, Units.HOUR_MINUTE_SEC);
                maxHeart = support.addLabelAndValue(composite, Messages.SummaryView_5, EMPTY, Units.BEATS_PER_MINUTE);
                avgHeart = support.addLabelAndValue(composite, Messages.SummaryView_6, EMPTY, Units.BEATS_PER_MINUTE);
                pace = support.addLabelAndValue(composite, Messages.SummaryView_7, EMPTY, getUnitFuerGeschwindigkeit(sport));

                trainingPerWeek = support.addLabelAndValue(composite, Messages.SummaryView_8, EMPTY, Units.NONE);
                trainingPerMonth = support.addLabelAndValue(composite, Messages.SummaryView_9, EMPTY, Units.NONE);
                kmPerWeek = support.addLabelAndValue(composite, Messages.SummaryView_10, EMPTY, Units.KM);
                kmPerMonth = support.addLabelAndValue(composite, Messages.SummaryView_11, EMPTY, Units.KM);

                section.setClient(composite);
            }
        });
    }

    void setModel(final SummaryModel model, final Sport sport) {
        this.model = model;
        update(sport);
    }

    private void update(final Sport sport) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (model != null) {
                    LOGGER.info(String.format("Update Summary View Part von %s", sport)); //$NON-NLS-1$
                    section.setExpanded(false);
                    if (model.getStartEnd() != null) {
                        final String start = TimeHelper.convertDateToString(model.getStartEnd().getStart().getMillis());
                        final String end = TimeHelper.convertDateToString(model.getStartEnd().getEnd().getMillis());
                        section.setDescription(NLS.bind(Messages.SummaryView_0, start, end));
                    }
                    distanz.setText(DistanceHelper.roundDistanceFromMeterToKm(model.getDistanz()));
                    dauer.setText(TimeHelper.convertTimeToStringHourUnlimited(1000 * model.getDauerInSeconds()));
                    maxHeart.setText(String.valueOf(model.getMaxHeart()));
                    avgHeart.setText(String.valueOf(model.getAvgHeart()));
                    if (sport.equals(Sport.BIKING)) {
                        pace.setText(DistanceHelper.calculatePace(model.getDistanz(), model.getDauerInSeconds(), Sport.BIKING));
                    } else {
                        pace.setText(DistanceHelper.calculatePace(model.getDistanz(), model.getDauerInSeconds(), Sport.RUNNING));
                    }
                    //
                    trainingPerWeek.setText(String.valueOf(model.getTrainingPerWeek()));
                    trainingPerMonth.setText(String.valueOf(model.getTrainingPerMonth()));
                    kmPerWeek.setText(String.valueOf(model.getKmPerWeek()));
                    kmPerMonth.setText(String.valueOf(model.getKmPerMonth()));
                    section.redraw();
                    section.setExpanded(true);
                    LOGGER.info("Update Summary View Part Finished"); //$NON-NLS-1$
                }
            }
        });

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
