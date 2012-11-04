package ch.opentrainingcenter.client.views.planung;

import java.util.ArrayList;
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
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.model.ModelFactory;
import ch.opentrainingcenter.client.model.planing.IPlanungWocheModel;
import ch.opentrainingcenter.client.model.planing.impl.PlanungModel;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
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

    public JahresplanungViewPart() {
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOG.debug("create JahresplanungViewPart"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        form.setText("Tralalaaaa");
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

        final Interval intervalStart = TimeHelper.getInterval(jahr, kw);
        final Interval intervalEnd = TimeHelper.getInterval(jahr, kw + 6);
        monat.setLayoutData(td);
        monat.setText("Trainingsplan von KW" + kw + " bis KW" + (kw + 6));
        monat.setDescription("6 Wochen Trainingsplan (vom " + intervalStart.getStart() + " bis " + intervalEnd.getEnd());

        final Composite composite = toolkit.createComposite(monat);
        final GridLayout layoutClient = new GridLayout(1, true);
        composite.setLayout(layoutClient);
        final boolean first = true;
        db = DatabaseAccessFactory.getDatabaseAccess();
        final List<IPlanungWoche> planungen = db.getPlanungsWoche(context.getAthlete(), jahr, kw, 6);
        final List<PlanungModel> pl = new ArrayList<PlanungModel>();
        for (final IPlanungWoche p : planungen) {
            pl.add(ModelFactory.createPlanungModel(p.getAthlete(), p.getJahr(), p.getKw(), p.getKmProWoche(), p.isInterval()));
        }

        final IPlanungWocheModel models = ModelFactory.createPlanungWochenModel(pl, context.getAthlete(), jahr, kw, 6);
        final List<KWViewPart> views = new ArrayList<KWViewPart>();

        KWViewPart kwViewPart;

        for (final PlanungModel model : models) {
            kwViewPart = new KWViewPart();
            kwViewPart.addLabelAndValue(composite, model);
            views.add(kwViewPart);
        }

        save = new Button(composite, SWT.PUSH);
        save.setText("Speichern");

        save.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(final Event event) {
                // speichern
                final List<IPlanungWoche> result = new ArrayList<IPlanungWoche>();
                for (final PlanungModel m : models) {
                    if (m.getKmProWoche() == 0) {
                        m.setInterval(false);
                    }
                    result.add(CommonTransferFactory.createIPlanungWoche(m.getAthlete(), m.getJahr(), m.getKw(), m.getKmProWoche(), m.isInterval()));
                }
                db.saveOrUpdate(result);
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
