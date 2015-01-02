package ch.opentrainingcenter.client.views.summary;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.summary.SummaryModel;
import ch.opentrainingcenter.transfer.Sport;

public class SummaryView extends ViewPart {
    private static final String EMPTY = ""; //$NON-NLS-1$

    public static final String ID = "ch.opentrainingcenter.client.views.summary.SummaryView"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(SummaryView.class);

    private FormToolkit toolkit;

    private SummaryModel model;

    private ScrolledForm form;

    private Section section;

    private FormToolkitSupport support;

    private Label distanz;

    private Label dauer;

    private Label maxHeart;

    private Label avgHeart;

    private Label pace;

    private Label trainingPerWeek;

    private Label trainingPerMonth;

    private Label kmPerWeek;

    private Label kmPerMonth;

    public SummaryView() {
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.info("create Summary view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        support = new FormToolkitSupport(toolkit);
        form = toolkit.createScrolledForm(parent);
        form.setText(Messages.SummaryView_1);
        toolkit.decorateFormHeading(form.getForm());

        final Composite body = form.getBody();
        GridLayoutFactory.swtDefaults().numColumns(1).applyTo(body);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL);
        addSection(body);

    }

    private void addSection(final Composite body) {
        section = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        section.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                // form.reflow(true);
            }
        });
        section.setText(Messages.SummaryView_2);
        GridLayoutFactory.swtDefaults().numColumns(1).applyTo(section);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.BEGINNING, SWT.BEGINNING).applyTo(section);

        final Composite composite = toolkit.createComposite(section);
        GridLayoutFactory.swtDefaults().numColumns(3).applyTo(composite);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.BEGINNING, SWT.BEGINNING).applyTo(composite);

        distanz = support.addLabelAndValue(composite, Messages.SummaryView_3, EMPTY, Units.KM);
        dauer = support.addLabelAndValue(composite, Messages.SummaryView_4, EMPTY, Units.HOUR_MINUTE_SEC);
        maxHeart = support.addLabelAndValue(composite, Messages.SummaryView_5, EMPTY, Units.BEATS_PER_MINUTE);
        avgHeart = support.addLabelAndValue(composite, Messages.SummaryView_6, EMPTY, Units.BEATS_PER_MINUTE);
        pace = support.addLabelAndValue(composite, Messages.SummaryView_7, EMPTY, Units.PACE);

        trainingPerWeek = support.addLabelAndValue(composite, Messages.SummaryView_8, EMPTY, Units.NONE);
        trainingPerMonth = support.addLabelAndValue(composite, Messages.SummaryView_9, EMPTY, Units.NONE);
        kmPerWeek = support.addLabelAndValue(composite, Messages.SummaryView_10, EMPTY, Units.KM);
        kmPerMonth = support.addLabelAndValue(composite, Messages.SummaryView_11, EMPTY, Units.KM);

        section.setClient(composite);
    }

    @Override
    public void setFocus() {

    }

    public void setRunData(final SummaryModel model) {
        this.model = model;
        update();
    }

    private void update() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (model != null) {
                    LOGGER.info("Update Summary View Part"); //$NON-NLS-1$
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
                    pace.setText(DistanceHelper.calculatePace(model.getDistanz(), model.getDauerInSeconds(), Sport.RUNNING));
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
}
