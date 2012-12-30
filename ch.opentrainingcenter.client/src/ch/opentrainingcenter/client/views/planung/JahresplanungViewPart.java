package ch.opentrainingcenter.client.views.planung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.cache.TrainingsPlanCache;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.planing.IPlanungWocheModel;
import ch.opentrainingcenter.model.planing.PlanungWocheComparator;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class JahresplanungViewPart extends ViewPart {
    private static final Logger LOG = Logger.getLogger(JahresplanungViewPart.class);
    public static final String ID = "ch.opentrainingcenter.client.views.planung.JahresplanungViewPart"; //$NON-NLS-1$

    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    private final ApplicationContext context = ApplicationContext.getApplicationContext();
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;
    private Button save;
    private IDatabaseAccess db;

    @Override
    public void createPartControl(final Composite parent) {
        LOG.debug("create JahresplanungViewPart"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        form.setText(Messages.JahresplanungViewPart_0);
        final Composite body = form.getBody();

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        body.setLayout(layout);

        addWeekSection(body);

    }

    private void addWeekSection(final Composite body) {

        final DateTime now = new DateTime();
        final int kw = now.getWeekOfWeekyear() + 1;
        final int jahr = now.getYear();
        final Section monat = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        monat.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.indent = 0;
        final int anzahl = store.getInt(PreferenceConstants.WEEK_FOR_PLAN);
        final Interval intervalStart = TimeHelper.getInterval(jahr, kw);
        final Interval intervalEnd = TimeHelper.getInterval(jahr, kw + anzahl);
        monat.setLayoutData(td);
        monat.setText(Messages.JahresplanungViewPart_1 + kw + Messages.JahresplanungViewPart_2 + (kw + anzahl));
        final String startDateString = TimeHelper.convertDateToString(intervalStart.getStart().toDate());
        final String endDateString = TimeHelper.convertDateToString(intervalEnd.getEnd().toDate());
        monat.setDescription(Messages.JahresplanungViewPart_3 + startDateString + Messages.JahresplanungViewPart_4 + endDateString + ")"); //$NON-NLS-1$

        final Composite composite = toolkit.createComposite(monat);
        final GridLayout layoutClient = new GridLayout(1, true);
        composite.setLayout(layoutClient);

        db = DatabaseAccessFactory.getDatabaseAccess();
        final List<IPlanungWoche> all = db.getPlanungsWoche(context.getAthlete(), jahr, kw);

        Collections.sort(all, Collections.reverseOrder(new PlanungWocheComparator()));
        final List<IPlanungWoche> planungen;
        if (all.size() > anzahl) {
            planungen = all.subList(0, anzahl);
        } else {
            planungen = all;
        }

        final List<IPlanungModel> pl = new ArrayList<IPlanungModel>();
        for (final IPlanungWoche p : planungen) {
            pl.add(ModelFactory.createPlanungModel(p.getAthlete(), p.getJahr(), p.getKw(), p.getKmProWoche(), p.isInterval(), p.getLangerLauf()));
        }

        final IPlanungWocheModel models = ModelFactory.createPlanungWochenModel(pl, context.getAthlete(), jahr, kw, anzahl);
        final List<KWViewPart> views = new ArrayList<KWViewPart>();

        KWViewPart kwViewPart;

        for (final IPlanungModel model : models) {
            kwViewPart = new KWViewPart();
            kwViewPart.addLabelAndValue(composite, model);
            views.add(kwViewPart);
        }

        save = new Button(composite, SWT.PUSH);
        save.setText(Messages.JahresplanungViewPart_5);

        save.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(final Event event) {
                // speichern
                final TrainingsPlanCache cache = TrainingsPlanCache.getInstance();
                final List<IPlanungWoche> result = new ArrayList<IPlanungWoche>();
                final List<IPlanungModel> list = new ArrayList<IPlanungModel>();
                for (final IPlanungModel m : models) {
                    final int km = m.getKmProWoche();
                    if (km == 0) {
                        m.setInterval(false);
                    }
                    final IAthlete athlete = m.getAthlete();
                    final int j = m.getJahr();
                    final int kwTmp = m.getKw();
                    final boolean interval = m.isInterval();
                    final int langerLauf = m.getLangerLauf();
                    final IPlanungWoche pla = CommonTransferFactory.createIPlanungWoche(athlete, j, kwTmp, km, interval, langerLauf);
                    result.add(pla);
                    list.add(m);
                }
                db.saveOrUpdate(result);
                cache.addAll(list);
            }
        });

        final GridData gdSave = new GridData();
        gdSave.horizontalAlignment = SWT.CENTER;
        gdSave.grabExcessHorizontalSpace = true;
        gdSave.verticalAlignment = SWT.BOTTOM;
        gdSave.horizontalIndent = 10;
        gdSave.verticalIndent = 20;
        save.setLayoutData(gdSave);

        // composite.setBackground(getViewSite().getWorkbenchWindow().getShell().getDisplay().getSystemColor(SWT.COLOR_CYAN));
        monat.setClient(composite);
    }

    interface Update {
        void onUpdate();
    }

    @Override
    public void setFocus() {

    }

}
