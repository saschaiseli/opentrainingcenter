package ch.opentrainingcenter.client.views.einstellungen;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.sportler.Sportler;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;

public class UserView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.views.einstellungen.user"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(UserView.class);
    private static final ApplicationContext ctx = ApplicationContext.getApplicationContext();

    private final Sportler sportler = new Sportler();
    private final IDatabaseAccess databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    private Text nameTf;
    private DateTime birthday;
    private Scale pulseScale;
    private Label lError;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;
    private Combo user;
    private Section selectSportler;
    private Section overviewSection;
    private final Map<Integer, Integer> dbIdMap = new HashMap<Integer, Integer>();
    private Button btnSave;

    private int index;

    private Composite parent;

    private Label scaledPulse;

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

        final List<IAthlete> allAthletes = databaseAccess.getAllAthletes();

        final Label sportlerLabel = new Label(sportlerComposite, SWT.NONE);
        sportlerLabel.setText(Messages.CreateAthleteView3);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = false;
        gridData.verticalIndent = 25;
        sportlerLabel.setLayoutData(gridData);
        //
        user = new Combo(sportlerComposite, SWT.NONE);
        index = 0;
        user.add(" ", index++); //$NON-NLS-1$
        for (final IAthlete athlete : allAthletes) {
            user.add(athlete.getName(), index);
            dbIdMap.put(index, athlete.getId());
            index++;
        }
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalIndent = 25;
        gridData.horizontalIndent = 5;
        user.setLayoutData(gridData);

        // Buttons
        final Button selectUser = new Button(sportlerComposite, SWT.PUSH);
        selectUser.setText(Messages.CreateAthleteView4);
        selectUser.setEnabled(false);
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.RIGHT;
        gridData.horizontalSpan = 2;
        gridData.verticalIndent = 25;
        gridData.grabExcessHorizontalSpace = false;
        selectUser.setLayoutData(gridData);
        selectUser.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final int index = user.getSelectionIndex();
                if (dbIdMap.containsKey(index)) {
                    final int dbId = dbIdMap.get(index);
                    final IAthlete athlete = databaseAccess.getAthlete(dbId);
                    final boolean confirm = MessageDialog.openConfirm(parent.getShell(), Messages.UserView_0, Messages.UserView_1);
                    if (confirm) {
                        store.setValue(PreferenceConstants.ATHLETE_ID, String.valueOf(dbId));
                        LOGGER.info(Messages.CreateAthleteView5 + athlete + " wird im Cache gesetzt."); //$NON-NLS-1$
                        ctx.setAthlete(athlete);
                        getViewSite().getWorkbenchWindow().getShell().setText(Application.WINDOW_TITLE + Messages.CreateAthleteView7 + athlete.getName());
                        PlatformUI.getWorkbench().restart();
                    }
                } else {
                    ctx.setAthlete(null);
                    getViewSite().getWorkbenchWindow().getShell().setText(Application.WINDOW_TITLE);
                }
            }

        });

        user.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                enableOrDisableButtonIfNoUserIsSelected(selectUser);
                final int selectionIndex = user.getSelectionIndex();
                if (dbIdMap.containsKey(selectionIndex)) {
                    final int dbId = dbIdMap.get(selectionIndex);
                    final IAthlete athlete = databaseAccess.getAthlete(dbId);
                    nameTf.setText(athlete.getName());
                    final Date date = athlete.getBirthday();
                    final org.joda.time.DateTime dt = new org.joda.time.DateTime(date.getTime());
                    birthday.setDate(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
                    final int maxHeart = athlete.getMaxHeartRate().intValue();
                    pulseScale.setSelection(maxHeart);
                    pulseScale.setToolTipText(NLS.bind(Messages.UserView_3, maxHeart));
                    scaledPulse.setText(NLS.bind(Messages.UserView_2, maxHeart));
                } else {
                    resetForm();
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
        overviewSection.setExpanded(ctx.getAthlete() == null);
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

        nameTf = new Text(overViewComposite, SWT.BORDER);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).span(2, 1).grab(true, true).applyTo(nameTf);

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
                sportler.setMaxHeartBeat(newValue);
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
                LOGGER.info("save " + sportler); //$NON-NLS-1$
                final Calendar cal = Calendar.getInstance(Locale.getDefault());
                cal.set(Calendar.YEAR, birthday.getYear());
                cal.set(Calendar.MONTH, birthday.getMonth() + 1);
                cal.set(Calendar.DAY_OF_MONTH, birthday.getDay());
                sportler.setMaxHeartBeat(pulseScale.getSelection());

                TrainingCache.getInstance().resetCache();

                final IAthlete exists = databaseAccess.getAthlete(sportler.getName());
                boolean confirm = true;

                if (exists != null) {
                    confirm = MessageDialog.openConfirm(parent.getShell(), Messages.UserView_4, NLS.bind(Messages.UserView_5, sportler.getName()));
                    if (confirm) {
                        exists.setBirthday(cal.getTime());
                        exists.setMaxHeartRate(sportler.getMaxHeartBeat());
                        save(exists);
                    }
                } else {
                    final IAthlete athlete = CommonTransferFactory.createAthlete(sportler.getName(), cal.getTime(), sportler.getMaxHeartBeat());
                    save(athlete);
                }
            }

            private void save(final IAthlete athlete) {
                databaseAccess.save(athlete);
                databaseAccess.saveOrUpdate(CommonTransferFactory.createRoute(athlete));
                if (!dbIdMap.containsValue(athlete.getId())) {
                    user.add(sportler.getName(), index);
                    dbIdMap.put(index, athlete.getId());
                    index++;
                }
                resetForm();
            }
        });
        GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.FILL).span(3, 1).grab(false, true).applyTo(btnSave);

        bindValues();

        overviewSection.setClient(overViewComposite);
    }

    private void enableOrDisableButtonIfNoUserIsSelected(final Button selectUser) {
        if (user.getSelectionIndex() >= 0) {
            selectUser.setEnabled(true);
        }
    }

    private void resetForm() {
        nameTf.setText(""); //$NON-NLS-1$
        final org.joda.time.DateTime dt = org.joda.time.DateTime.now();
        birthday.setDate(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
        pulseScale.setSelection(0);
        scaledPulse.setText(NLS.bind(Messages.UserView_2, 0));
        lError.setText(""); //$NON-NLS-1$
        user.setFocus();
    }

    private void bindValues() {
        final DataBindingContext ctx = new DataBindingContext();

        // name
        final IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(nameTf);
        final IObservableValue modelValue = BeanProperties.value(Sportler.class, "name").observe(sportler); //$NON-NLS-1$
        final UpdateValueStrategy strategyName = new UpdateValueStrategy();
        strategyName.setBeforeSetValidator(new SportlerValidator(5));
        final Binding bindName = ctx.bindValue(widgetValue, modelValue, strategyName, null);

        ControlDecorationSupport.create(bindName, SWT.TOP | SWT.RIGHT);
        // -----------------------

        final IObservableValue errorObservable = WidgetProperties.text().observe(lError);
        // This one listenes to all changes
        ctx.bindValue(errorObservable, new AggregateValidationStatus(ctx.getBindings(), AggregateValidationStatus.MAX_SEVERITY), null, null);
        errorObservable.addChangeListener(new IChangeListener() {

            @Override
            public void handleChange(final ChangeEvent event) {
                if (Status.OK_STATUS.getMessage().equals(lError.getText())) {
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

}
