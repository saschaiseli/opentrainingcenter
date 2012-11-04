package ch.opentrainingcenter.client.views.planung;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
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
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class JahresplanungViewPart extends ViewPart {
    private static final Logger LOGGER = Logger.getLogger(JahresplanungViewPart.class);
    public static final String ID = "ch.opentrainingcenter.client.views.planung.JahresplanungViewPart"; //$NON-NLS-1$

    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    private final ApplicationContext context = ApplicationContext.getApplicationContext();
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;
    private final Image lock = Activator.getImageDescriptor(IImageKeys.LOCK).createImage();
    private final Image lockOpen = Activator.getImageDescriptor(IImageKeys.LOCK_OPEN).createImage();

    public JahresplanungViewPart() {
    }

    @Override
    public void createPartControl(final Composite parent) {

        LOGGER.debug("create JahresplanungViewPart"); //$NON-NLS-1$
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
        boolean first = true;
        final List<IPlanungWoche> planungen = DatabaseAccessFactory.getDatabaseAccess().getPlanungsWoche(context.getAthlete(), jahr, kw, 6);

        final IPlanungWocheModel models = ModelFactory.createPlanungsModel(planungen, context.getAthlete(), jahr, kw, 6);

        for (final IPlanungWoche model : models) {
            addLabelAndValue(composite, model, first);
            first = false;
        }
        final Button save = new Button(composite, SWT.PUSH);
        save.setText("Speichern");

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

    private Scale addLabelAndValue(final Composite parent, final IPlanungWoche planung, final boolean first) {

        final String label = "KW" + planung.getKw();
        final int value = planung.getKmProWoche();
        final boolean enable = planung.isActive();
        final boolean interval = planung.isInterval();

        final Composite c = new Composite(parent, SWT.NONE);
        // c.setBackground(getViewSite().getWorkbenchWindow().getShell().getDisplay().getSystemColor(SWT.COLOR_CYAN));
        final GridLayout layout = new GridLayout(5, false);
        c.setLayout(layout);

        final GridData gd = new GridData();
        gd.horizontalIndent = 10;
        gd.verticalIndent = 40;
        c.setLayoutData(gd);

        // Label
        final GridData laGd = new GridData();
        final Label kw = new Label(c, SWT.NONE);
        kw.setText(label);
        laGd.horizontalIndent = 10;
        kw.setLayoutData(laGd);

        final Scale scale = new Scale(c, SWT.BORDER);
        scale.setMaximum(60);
        scale.setMinimum(0);
        scale.setIncrement(1);
        scale.setPageIncrement(5);
        scale.setSelection(value);
        final GridData gd1 = new GridData();
        gd1.minimumWidth = 400;
        gd1.grabExcessHorizontalSpace = true;
        scale.setLayoutData(gd1);

        // einheit
        final Label kmProWoche = new Label(c, SWT.NONE);
        kmProWoche.setText(value + " km");
        scale.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                final int newValue = scale.getSelection();
                kmProWoche.setText(newValue + " km");
                planung.setKmProWoche(newValue);
            }
        });
        final GridData gdKm = new GridData();
        gdKm.minimumWidth = 50;
        gdKm.grabExcessHorizontalSpace = true;
        gdKm.horizontalAlignment = SWT.FILL;
        gdKm.horizontalIndent = 5;
        kmProWoche.setLayoutData(gdKm);

        final GridData gdButton = new GridData();
        gdButton.horizontalAlignment = SWT.CENTER;

        final Button buttonInterval = new Button(c, SWT.CHECK);
        buttonInterval.setText("Intervall Training");
        buttonInterval.setLayoutData(gdButton);
        buttonInterval.setSelection(interval);

        buttonInterval.addSelectionListener(new SelectionListener() {
            boolean toggle = buttonInterval.getSelection();

            @Override
            public void widgetSelected(final SelectionEvent e) {
                planung.setInterval(toggle);
                toggle = !toggle;
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {

            }
        });

        final Label lockLabel = new Label(c, SWT.NONE);
        lockLabel.setImage(lockOpen);
        lockLabel.setToolTipText("Doppelclick um den Record zu sperren");
        final GridData gdLabel = new GridData();
        gdLabel.widthHint = 120;
        gdLabel.horizontalIndent = 0;
        lockLabel.setLayoutData(gdLabel);

        lockLabel.addMouseListener(new MouseListener() {
            boolean toggle = !enable;

            @Override
            public void mouseUp(final MouseEvent e) {
            }

            @Override
            public void mouseDown(final MouseEvent e) {
            }

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
                if (toggle) {
                    lockLabel.setImage(lockOpen);
                } else {
                    lockLabel.setImage(lock);
                }
                scale.setEnabled(toggle);
                kw.setEnabled(toggle);
                kmProWoche.setEnabled(toggle);
                buttonInterval.setEnabled(toggle);
                toggle = !toggle;
            }
        });

        return scale;
    }

    @Override
    public void setFocus() {

    }

}
