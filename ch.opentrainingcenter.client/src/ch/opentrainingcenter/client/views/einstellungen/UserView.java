package ch.opentrainingcenter.client.views.einstellungen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.client.cache.AthleteCache;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class UserView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.views.einstellungen.user"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(UserView.class);
    private static final ApplicationContext ctx = ApplicationContext.getApplicationContext();

    private final AthleteCache athleteCache = AthleteCache.getInstance();
    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    private Text userName;
    private DateTime birthday;
    private Scale pulseScale;
    private Label lError;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;
    private Section selectSportler;
    private Section overviewSection;

    private Button btnSave;

    private Composite parent;

    private Label scaledPulse;

    private ControlDecoration deco;

    private final IAthlete currentAthlete;

    private final List<Object> allAthletes = new ArrayList<>();

    private final IDatabaseAccess databaseAccess;

    public UserView() {
        currentAthlete = ctx.getAthlete();
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
    }

    @Override
    public void createPartControl(final Composite parentComposite) {
        this.parent = parentComposite;
        LOGGER.debug("Create User View"); //$NON-NLS-1$
        toolkit = new FormToolkit(this.parent.getDisplay());

        form = toolkit.createScrolledForm(this.parent);
        toolkit.decorateFormHeading(form.getForm());
        form.setText(Messages.CreateAthleteView0);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        final Composite body = form.getBody();
        body.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        body.setLayoutData(td);

        createSelectSportler(body);
        createAddSportler(body);
    }

    private void createSelectSportler(final Composite body) {
        selectSportler = toolkit.createSection(body, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
                | ExpandableComposite.EXPANDED);
        selectSportler.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        selectSportler.setExpanded(ctx.getAthlete() == null);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        selectSportler.setLayoutData(td);
        selectSportler.setText(Messages.CreateAthleteView1);
        selectSportler.setDescription(Messages.CreateAthleteView2);

        final Composite sportlerComposite = toolkit.createComposite(selectSportler);
        final GridLayout layoutClient = new GridLayout(2, false);
        sportlerComposite.setLayout(layoutClient);

        allAthletes.add(new Object());
        allAthletes.addAll(athleteCache.getAll());

        final Label sportlerLabel = new Label(sportlerComposite, SWT.NONE);
        sportlerLabel.setText(Messages.CreateAthleteView3);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = false;
        gridData.verticalIndent = 25;
        sportlerLabel.setLayoutData(gridData);
        //

        final ComboViewer viewer = new ComboViewer(sportlerComposite, SWT.BORDER | SWT.READ_ONLY);
        viewer.setContentProvider(ArrayContentProvider.getInstance());
        viewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(final Object element) {
                if (element instanceof IAthlete) {
                    final IAthlete athlete = (IAthlete) element;
                    return athlete.getName();
                }
                return Messages.UserView_9;
            }
        });
        viewer.setInput(allAthletes);
        if (currentAthlete != null) {
            viewer.setSelection(new StructuredSelection(currentAthlete));
        } else {
            viewer.setSelection(new StructuredSelection(allAthletes.get(0)));
        }
        getSite().setSelectionProvider(viewer);

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalIndent = 25;
        gridData.horizontalIndent = 5;
        viewer.getCombo().setLayoutData(gridData);

        // Buttons
        final Button btnSelectUser = new Button(sportlerComposite, SWT.PUSH);
        btnSelectUser.setText(Messages.CreateAthleteView4);
        btnSelectUser.setEnabled(false);
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.RIGHT;
        gridData.horizontalSpan = 2;
        gridData.verticalIndent = 25;
        gridData.grabExcessHorizontalSpace = false;
        btnSelectUser.setLayoutData(gridData);
        btnSelectUser.setEnabled(false);
        btnSelectUser.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                boolean restart = false;
                if (selection != null) {
                    final IAthlete athlete = (IAthlete) selection.getFirstElement();
                    final boolean confirm = MessageDialog.openConfirm(parent.getShell(), Messages.UserView_0, Messages.UserView_1);
                    if (confirm) {
                        store.setValue(PreferenceConstants.ATHLETE_ID, String.valueOf(athlete.getId()));
                        LOGGER.info(NLS.bind("Benutzer {0} wird in Preferences gesetzt", athlete)); //$NON-NLS-1$
                        ctx.setAthlete(athlete);
                        getViewSite().getWorkbenchWindow().getShell().setText(Application.WINDOW_TITLE + Messages.CreateAthleteView7 + athlete.getName());
                        restart = true;
                    }
                }
                if (restart) {
                    PlatformUI.getWorkbench().restart();
                } else {
                    sportlerComposite.setFocus();
                }
            }
        });

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection.getFirstElement() instanceof IAthlete) {
                    btnSelectUser.setEnabled(true);
                    final IAthlete selectedAthlete = (IAthlete) selection.getFirstElement();
                    setAthleteForm(selectedAthlete);
                } else {
                    resetAthleteForm();
                }
            }
        });

        final Label spacer = new Label(sportlerComposite, SWT.NONE);
        spacer.setText(""); //$NON-NLS-1$

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = false;
        gridData.horizontalSpan = 2;
        gridData.widthHint = 100;
        spacer.setLayoutData(gridData);

        selectSportler.setClient(sportlerComposite);
    }

    private void createAddSportler(final Composite body) {

        overviewSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        overviewSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        overviewSection.setExpanded(true);
        td = new TableWrapData();
        td.colspan = 1;
        overviewSection.setLayoutData(td);
        overviewSection.setText(Messages.CreateAthleteView8);
        overviewSection.setDescription(Messages.CreateAthleteView9);

        final Composite overViewComposite = toolkit.createComposite(overviewSection);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);
        // ---------------------------------------------------------------------
        // name
        final Label lName = new Label(overViewComposite, SWT.NONE);
        lName.setText(Messages.CreateAthleteView10);
        GridData gd = GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).create();// applyTo(lName);
        gd.minimumWidth = 30;
        GridDataFactory.createFrom(gd).applyTo(lName);

        userName = new Text(overViewComposite, SWT.BORDER);
        deco = new ControlDecoration(userName, SWT.LEFT | SWT.TOP);
        final FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
        deco.setImage(fieldDecoration.getImage());
        deco.setShowOnlyOnFocus(false);
        deco.hide();

        userName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                final String name = userName.getText();
                if (name != null && name.length() >= 5) {
                    final IAthlete exists = athleteCache.get(name);
                    if (exists != null) {
                        handleDecoration(NLS.bind(Messages.UserView_6, name));
                    } else {
                        handleDecoration(null);
                    }
                } else {
                    handleDecoration(Messages.UserView_7);
                }
            }
        });

        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).span(2, 1).grab(true, true).applyTo(userName);

        // ---------------------------------------------------------------------
        // pulse
        final Label lPulse = new Label(overViewComposite, SWT.NONE);
        lPulse.setText(Messages.CreateAthleteView12);

        final Composite pulseComp = new Composite(overViewComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(pulseComp);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(2, 1).applyTo(pulseComp);

        pulseScale = new Scale(pulseComp, SWT.BORDER);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(pulseScale);

        pulseScale.setMinimum(0);
        pulseScale.setMaximum(220);
        pulseScale.setIncrement(1);
        pulseScale.setPageIncrement(5);
        pulseScale.setSelection(160);
        pulseScale.setToolTipText(NLS.bind(Messages.UserView_3, pulseScale.getSelection()));
        pulseScale.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                final int newValue = pulseScale.getSelection();
                scaledPulse.setText(NLS.bind(Messages.UserView_2, newValue));
                pulseScale.setToolTipText(NLS.bind(Messages.UserView_3, pulseScale.getSelection()));
            }
        });

        scaledPulse = new Label(pulseComp, SWT.NONE);
        gd = GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.FILL).grab(false, true).create();
        gd.minimumWidth = 10;
        GridDataFactory.createFrom(gd).applyTo(scaledPulse);
        scaledPulse.setText(NLS.bind(Messages.UserView_2, 160));

        // ---------------------------------------------------------------------
        // alter
        final Label lBirthday = new Label(overViewComposite, SWT.NONE);
        lBirthday.setText(Messages.CreateAthleteView11);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.FILL).grab(true, true).span(2, 1).applyTo(lBirthday);

        birthday = new DateTime(overViewComposite, SWT.CALENDAR | SWT.BORDER);
        gd = GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.FILL).grab(true, true).create();
        gd.minimumWidth = 220;
        GridDataFactory.createFrom(gd).applyTo(birthday);

        lError = new Label(overViewComposite, SWT.NONE);
        lError.setVisible(false);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).span(2, 1).applyTo(lError);

        btnSave = new Button(overViewComposite, SWT.PUSH);
        btnSave.setText(Messages.CreateAthleteView16);
        btnSave.setEnabled(false);
        btnSave.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final String name = userName.getText();
                LOGGER.info(String.format("save %s", name)); //$NON-NLS-1$

                final int maxPulse = pulseScale.getSelection();
                final Calendar cal = Calendar.getInstance(Locale.getDefault());
                cal.set(Calendar.YEAR, birthday.getYear());
                cal.set(Calendar.MONTH, birthday.getMonth() + 1);
                cal.set(Calendar.DAY_OF_MONTH, birthday.getDay());

                final IAthlete exists = athleteCache.get(name);

                if (exists != null) {
                    final boolean confirm;
                    confirm = MessageDialog.openConfirm(parent.getShell(), Messages.UserView_4, NLS.bind(Messages.UserView_5, name));
                    if (confirm) {
                        exists.setBirthday(cal.getTime());
                        exists.setMaxHeartRate(maxPulse);
                        save(exists);
                        athleteCache.add(exists);
                    }
                } else {
                    // neuen hinzufügen
                    final IAthlete athlete = CommonTransferFactory.createAthlete(name, cal.getTime(), maxPulse);
                    save(athlete);
                    athleteCache.add(athlete);
                    allAthletes.add(athlete);
                }
            }

            private void save(final IAthlete athlete) {
                databaseAccess.save(athlete);
                databaseAccess.saveOrUpdate(CommonTransferFactory.createDefaultRoute(athlete));
                if (currentAthlete == null) {
                    resetAthleteForm();
                }
            }
        });
        GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.FILL).span(3, 1).grab(false, true).applyTo(btnSave);

        if (ctx.getAthlete() != null) {
            setAthleteForm(ctx.getAthlete());
            userName.setEnabled(false);
            pulseScale.setEnabled(true);
            birthday.setEnabled(true);
            btnSave.setEnabled(true);
        }
        overviewSection.setClient(overViewComposite);
    }

    private void setAthleteForm(final IAthlete athlete) {
        userName.setText(athlete.getName());
        final Date date = athlete.getBirthday();
        final org.joda.time.DateTime dt = new org.joda.time.DateTime(date.getTime());
        birthday.setDate(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
        final int maxHeart = athlete.getMaxHeartRate().intValue();
        pulseScale.setSelection(maxHeart);
        pulseScale.setToolTipText(NLS.bind(Messages.UserView_3, maxHeart));
        scaledPulse.setText(NLS.bind(Messages.UserView_2, maxHeart));

        userName.setEnabled(false);
        pulseScale.setEnabled(false);
        deco.hide();
    }

    private void resetAthleteForm() {
        userName.setText(""); //$NON-NLS-1$
        final org.joda.time.DateTime dt = org.joda.time.DateTime.now();
        birthday.setDate(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
        pulseScale.setSelection(0);
        scaledPulse.setText(NLS.bind(Messages.UserView_2, 0));
        lError.setText(""); //$NON-NLS-1$
        parent.setFocus();

        userName.setEnabled(true);
        pulseScale.setEnabled(true);
        handleDecoration(Messages.UserView_8);
    }

    private void handleDecoration(final String message) {
        if (message == null) {
            // alles ok, kann gespeichert werden
            deco.hide();
            btnSave.setEnabled(true);
        } else {
            deco.show();
            deco.setDescriptionText(message);
            btnSave.setEnabled(false);
        }
    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

}
