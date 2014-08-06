package ch.opentrainingcenter.client.views.weeks;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.training.IOverviewModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class MonthlyWeekOverview extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.weeks.monthlyOverview"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(MonthlyWeekOverview.class);

    private final IDatabaseAccess databaseAccess;

    private final IAthlete athlete;

    private final Cache cache;

    private FormToolkit toolkit;

    private TableWrapData td;

    private Section section, sectionWeek;

    private Label kmTotal, kmTotalWeek;

    private Label zeitTotal, zeitTotalWeek;

    private Label anzahl, anzahlWeek;

    private Composite weekComposite;

    public MonthlyWeekOverview() {
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        athlete = ApplicationContext.getApplicationContext().getAthlete();
        cache = TrainingCache.getInstance();
    }

    @Override
    public void createPartControl(final Composite parent) {

        LOGGER.debug("create WeeklyOverview "); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());

        final ScrolledForm form = toolkit.createScrolledForm(parent);

        toolkit.decorateFormHeading(form.getForm());

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        final Composite body = form.getBody();
        body.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        body.setLayoutData(td);
        form.setText(Messages.MonthlyOverview_0);

        addWeek(body);
        addMonth(body);

        update();
        addListener();
    }

    private void addWeek(final Composite body) {
        sectionWeek = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        sectionWeek.setExpanded(false);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;

        sectionWeek.setLayoutData(td);
        sectionWeek.setText(NLS.bind(Messages.WeeklyOverview_3, DateTime.now().getWeekOfWeekyear()));

        weekComposite = toolkit.createComposite(sectionWeek);
        final GridLayout layoutClient = new GridLayout(3, false);
        weekComposite.setLayout(layoutClient);

        final FormToolkitSupport support = new FormToolkitSupport(toolkit);

        anzahlWeek = support.addLabelAndValue(weekComposite, Messages.WeeklyOverview_2, "", Units.NONE); //$NON-NLS-1$
        zeitTotalWeek = support.addLabelAndValue(weekComposite, Messages.WeeklyOverview_6, "", Units.NONE); //$NON-NLS-1$
        kmTotalWeek = support.addLabelAndValue(weekComposite, Messages.WeeklyOverview_4, "", Units.NONE); //$NON-NLS-1$

        sectionWeek.setClient(weekComposite);
    }

    private void addMonth(final Composite body) {
        section = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        section.setExpanded(false);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;

        section.setLayoutData(td);
        section.setText(TimeHelper.getTranslatedMonat(DateTime.now()));

        weekComposite = toolkit.createComposite(section);
        final GridLayout layoutClient = new GridLayout(3, false);

        weekComposite.setLayout(layoutClient);

        final FormToolkitSupport support = new FormToolkitSupport(toolkit);

        anzahl = support.addLabelAndValue(weekComposite, Messages.WeeklyOverview_2, "", Units.NONE); //$NON-NLS-1$
        zeitTotal = support.addLabelAndValue(weekComposite, Messages.WeeklyOverview_6, "", Units.NONE); //$NON-NLS-1$
        kmTotal = support.addLabelAndValue(weekComposite, Messages.WeeklyOverview_4, "", Units.NONE); //$NON-NLS-1$

        section.setClient(weekComposite);
    }

    private void update() {
        final DateTime now = DateTime.now();
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                LOGGER.info("update WeeklyOverview"); //$NON-NLS-1$
                updateFields(TimeHelper.getFirstDayOfWeek(now), kmTotalWeek, zeitTotalWeek, anzahlWeek, sectionWeek);
                LOGGER.info("update MonthlyOverview"); //$NON-NLS-1$
                updateFields(TimeHelper.getFirstDayOfMonth(now), kmTotal, zeitTotal, anzahl, section);
            }

            private void updateFields(final DateTime firstDay, final Label km, final Label zeit, final Label anzahl, final Section section) {
                final List<ITraining> trainingsWeek = databaseAccess.getTrainingsByAthleteAndDate(athlete, firstDay, now);
                final IOverviewModel model = ModelFactory.createOverview(trainingsWeek);
                km.setText(String.valueOf(DistanceHelper.roundDistanceFromMeterToKm(model.getTotaleDistanzInMeter(Sport.RUNNING))));
                zeit.setText(TimeHelper.convertTimeToString(model.getTotaleZeitInSekunden(Sport.RUNNING) * 1000));
                anzahl.setText(String.valueOf(model.getAnzahlTrainings(Sport.RUNNING)));
                section.layout();
                section.setExpanded(true);
            }
        });
    }

    private void addListener() {
        cache.addListener(new IRecordListener<ITraining>() {

            @Override
            public void recordChanged(final Collection<ITraining> entry) {
                update();
            }

            @Override
            public void deleteRecord(final Collection<ITraining> entry) {
                update();
            }
        });
    }

    @Override
    public void setFocus() {
        section.setFocus();
    }

}
