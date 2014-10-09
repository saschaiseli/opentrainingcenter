package ch.opentrainingcenter.client.views.weeks;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.training.IOverviewModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class MonthWeekTabItem {

    private static class OverviewWidgets {
        private final Label km;
        private final Label zeit;
        private final Label anzahl;
        private final Section section;

        OverviewWidgets(final Label km, final Label zeit, final Label anzahl, final Section section) {
            this.km = km;
            this.zeit = zeit;
            this.anzahl = anzahl;
            this.section = section;
        }

        public Label getKm() {
            return km;
        }

        public Label getZeit() {
            return zeit;
        }

        public Label getAnzahl() {
            return anzahl;
        }

        public Section getSection() {
            return section;
        }
    }

    private final Sport sport;
    private final IDatabaseAccess databaseAccess;
    private final IAthlete athlete;
    private FormToolkit toolkit;
    private final Cache cache;
    private OverviewWidgets week;
    private OverviewWidgets month;

    public MonthWeekTabItem(final Sport sport, final IAthlete athlete, final IDatabaseAccess databaseAccess) {
        this.sport = sport;
        this.athlete = athlete;
        this.databaseAccess = databaseAccess;
        cache = TrainingCache.getInstance();
    }

    public void createPartControl(final CTabFolder folder) {
        final CTabItem item = new CTabItem(folder, SWT.NONE);
        item.setText(sport.getTranslated());

        toolkit = new FormToolkit(folder.getDisplay());

        final ScrolledForm form = toolkit.createScrolledForm(folder);
        GridLayoutFactory.swtDefaults().applyTo(form);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(form);

        item.setControl(form);
        toolkit.decorateFormHeading(form.getForm());
        form.setText(sport.getTranslated());

        final Composite body = form.getBody();
        GridLayoutFactory.swtDefaults().applyTo(body);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(body);

        week = createSection(body, NLS.bind(Messages.WeeklyOverview_3, DateTime.now().getWeekOfWeekyear()), true);
        month = createSection(body, TimeHelper.getTranslatedMonat(DateTime.now()), false);

        final DateTime now = DateTime.now();
        update(now);
        addListener(now);

        Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent event) {
                update(now);
            }
        });
    }

    private OverviewWidgets createSection(final Composite body, final String title, final boolean week) {
        final Section section = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        section.setExpanded(false);
        section.setText(title);
        GridLayoutFactory.swtDefaults().applyTo(section);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(section);

        final Composite composite = toolkit.createComposite(section);
        GridLayoutFactory.swtDefaults().numColumns(3).applyTo(composite);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(composite);

        final FormToolkitSupport support = new FormToolkitSupport(toolkit);
        final Label anzahl = support.addLabelAndValue(composite, Messages.WeeklyOverview_2, "", Units.NONE); //$NON-NLS-1$
        final Label zeit = support.addLabelAndValue(composite, Messages.WeeklyOverview_6, "", Units.NONE); //$NON-NLS-1$
        final Label km = support.addLabelAndValue(composite, Messages.WeeklyOverview_4, "", Units.NONE); //$NON-NLS-1$ 
        section.setClient(composite);
        return new OverviewWidgets(km, zeit, anzahl, section);
    }

    private void addListener(final DateTime now) {
        cache.addListener(new IRecordListener<ITraining>() {

            @Override
            public void recordChanged(final Collection<ITraining> entry) {
                update(now);
            }

            @Override
            public void deleteRecord(final Collection<ITraining> entry) {
                update(now);
            }
        });
    }

    public void update(final DateTime end) {
        final DateTime firstDayOfWeek = TimeHelper.getFirstDayOfWeek(end);
        final DateTime firstDayOfMonth = TimeHelper.getFirstDayOfMonth(end);

        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                if (week != null && !week.getSection().isDisposed()) {
                    week.getSection().setExpanded(false);
                    final List<ITraining> trainingsWeek = databaseAccess.getTrainingsByAthleteAndDate(athlete, firstDayOfWeek, end);
                    update(trainingsWeek, week);
                    week.getSection().setExpanded(true);

                    month.getSection().setExpanded(false);
                    final List<ITraining> trainingsMonth = databaseAccess.getTrainingsByAthleteAndDate(athlete, firstDayOfMonth, end);
                    update(trainingsMonth, month);
                    month.getSection().setExpanded(true);
                }
            }
        });
    }

    private void update(final List<ITraining> trainings, final OverviewWidgets widget) {
        final IOverviewModel model = ModelFactory.createOverview(trainings);
        widget.getKm().setText(String.valueOf(DistanceHelper.roundDistanceFromMeterToKm(model.getTotaleDistanzInMeter(sport))));
        widget.getZeit().setText(TimeHelper.convertTimeToString(model.getTotaleZeitInSekunden(sport) * 1000));
        widget.getAnzahl().setText(String.valueOf(model.getAnzahlTrainings(sport)));
    }

    public Sport getSport() {
        return sport;
    }
}
