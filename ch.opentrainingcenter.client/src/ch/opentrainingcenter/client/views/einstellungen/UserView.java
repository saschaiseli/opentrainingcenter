package ch.opentrainingcenter.client.views.einstellungen;

import java.util.HashMap;
import java.util.List;
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
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.ahtlete.SportlerValidator;
import ch.opentrainingcenter.client.views.databinding.NumberValidator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.sportler.Sportler;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;

public class UserView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.views.einstellungen.user"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(UserView.class);
    private final Sportler sportler = new Sportler();
    private final IDatabaseAccess databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    private Text nameTf;
    private Text ageTf;
    private Text pulseTf;
    private Label errorLabel;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;
    private Combo user;
    private Section selectSportler;
    private Section overviewSection;
    private final Map<Integer, Integer> indexOfSelectBoxMappedToDatabaseId = new HashMap<Integer, Integer>();
    private Button createButton;

    private int index;

    private Composite parent;

    @Override
    public void createPartControl(final Composite parentComposite) {
        this.parent = parentComposite;
        LOGGER.debug("Create User View"); //$NON-NLS-1$
        toolkit = new FormToolkit(this.parent.getDisplay());
        form = toolkit.createScrolledForm(this.parent);
        // form.setSize(1000, 2000);
        // gridlayout definieren

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        final Composite body = form.getBody();
        body.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        body.setLayoutData(td);
        form.setText(Messages.CreateAthleteView0);

        createSelectSportler(body);
        createAddSportler(body);
    }

    private void createSelectSportler(final Composite body) {
        selectSportler = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        selectSportler.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        selectSportler.setExpanded(true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        selectSportler.setLayoutData(td);
        selectSportler.setText(Messages.CreateAthleteView1);
        selectSportler.setDescription(Messages.CreateAthleteView2);

        final Composite sportlerComposite = toolkit.createComposite(selectSportler);
        final GridLayout layoutClient = new GridLayout(2, false);
        sportlerComposite.setLayout(layoutClient);

        final List<IAthlete> allAthletes = DatabaseAccessFactory.getDatabaseAccess().getAllAthletes();

        // gender
        final Label sportlerLabel = new Label(sportlerComposite, SWT.NONE);
        sportlerLabel.setText(Messages.CreateAthleteView3);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = false;
        sportlerLabel.setLayoutData(gridData);
        //
        user = new Combo(sportlerComposite, SWT.NONE);
        index = 0;
        for (final IAthlete athlete : allAthletes) {
            user.add(athlete.getName(), index);
            indexOfSelectBoxMappedToDatabaseId.put(index, athlete.getId());
            index++;
        }

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = SWT.LEFT;
        gridData.horizontalIndent = 5;
        user.setLayoutData(gridData);

        final Button selectUser = new Button(sportlerComposite, SWT.PUSH);
        selectUser.setText(Messages.CreateAthleteView4);
        selectUser.setEnabled(false);
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = false;
        gridData.horizontalAlignment = SWT.LEFT;
        selectUser.setLayoutData(gridData);
        selectUser.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final int selectionIndex = user.getSelectionIndex();
                final int dbId = indexOfSelectBoxMappedToDatabaseId.get(selectionIndex);
                store.setValue(PreferenceConstants.ATHLETE_ID, String.valueOf(dbId));

                final IAthlete athlete = databaseAccess.getAthlete(dbId);
                LOGGER.info(Messages.CreateAthleteView5 + athlete + " wird im Cache gesetzt."); //$NON-NLS-1$
                ApplicationContext.getApplicationContext().setAthlete(athlete);
                getViewSite().getWorkbenchWindow().getShell().setText(Application.WINDOW_TITLE + Messages.CreateAthleteView7 + athlete.getName());

                final boolean confirm = MessageDialog.openConfirm(parent.getShell(), Messages.UserView_0, Messages.UserView_1);
                if (confirm) {
                    PlatformUI.getWorkbench().restart();
                }
            }

        });

        final Button editUser = new Button(sportlerComposite, SWT.PUSH);
        editUser.setText(Messages.UserView_2);
        editUser.setEnabled(false);
        editUser.setLayoutData(gridData);
        editUser.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final int selectionIndex = user.getSelectionIndex();
                final int dbId = indexOfSelectBoxMappedToDatabaseId.get(selectionIndex);

                final IAthlete athlete = databaseAccess.getAthlete(dbId);
                nameTf.setText(athlete.getName());
                ageTf.setText(athlete.getAge().toString());
                pulseTf.setText(athlete.getMaxHeartRate().toString());
            }
        });

        user.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                enableOrDisableButtonIfNoUserIsSelected(selectUser, editUser);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {

            }
        });

        user.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(final FocusEvent e) {
                enableOrDisableButtonIfNoUserIsSelected(selectUser, editUser);
            }

            @Override
            public void focusGained(final FocusEvent e) {

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
        final GridLayout layoutClient = new GridLayout(2, false);
        overViewComposite.setLayout(layoutClient);

        // name
        final Label firstLabel = new Label(overViewComposite, SWT.NONE);
        firstLabel.setText(Messages.CreateAthleteView10);
        nameTf = new Text(overViewComposite, SWT.BORDER);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 5;
        nameTf.setLayoutData(gridData);

        // alter
        final Label ageLabel = new Label(overViewComposite, SWT.NONE);
        ageLabel.setText(Messages.CreateAthleteView11);
        ageTf = new Text(overViewComposite, SWT.BORDER);

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 5;
        ageTf.setLayoutData(gridData);

        // pulse
        final Label pulseLabel = new Label(overViewComposite, SWT.NONE);
        pulseLabel.setText(Messages.CreateAthleteView12);
        pulseTf = new Text(overViewComposite, SWT.BORDER);

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 5;
        pulseTf.setLayoutData(gridData);

        errorLabel = new Label(overViewComposite, SWT.NONE);
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        errorLabel.setLayoutData(gridData);
        errorLabel.setVisible(false);

        createButton = new Button(overViewComposite, SWT.PUSH);
        createButton.setText(Messages.CreateAthleteView16);
        createButton.setEnabled(false);
        createButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                LOGGER.info("save " + sportler); //$NON-NLS-1$
                final IAthlete athlete = CommonTransferFactory.createAthlete(sportler.getName(), sportler.getAge(), sportler.getMaxHeartBeat());
                try {
                    TrainingCenterDataCache.getInstance().resetCache();
                    DatabaseAccessFactory.getDatabaseAccess().save(athlete);
                    resetForm();
                    user.add(athlete.getName(), index);
                    indexOfSelectBoxMappedToDatabaseId.put(index, athlete.getId());
                    index++;
                } catch (final Exception e1) {
                    errorLabel.setText(Messages.CreateAthleteView17);
                }
            }

        });
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.LEFT;
        gridData.grabExcessHorizontalSpace = false;
        gridData.horizontalIndent = 5;
        createButton.setLayoutData(gridData);

        bindValues();

        overviewSection.setClient(overViewComposite);
    }

    private void enableOrDisableButtonIfNoUserIsSelected(final Button selectUser, final Button edit) {
        if (user.getSelectionIndex() >= 0) {
            selectUser.setEnabled(true);
            edit.setEnabled(true);
        }
    }

    private void resetForm() {
        nameTf.setText(""); //$NON-NLS-1$
        ageTf.setText(""); //$NON-NLS-1$
        pulseTf.setText(""); //$NON-NLS-1$
        errorLabel.setText(""); //$NON-NLS-1$
    }

    private void bindValues() {
        // The DataBindingContext object will manage the databindings
        final DataBindingContext ctx = new DataBindingContext();

        // name
        IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(nameTf);
        IObservableValue modelValue = BeanProperties.value(Sportler.class, "name").observe(sportler); //$NON-NLS-1$
        final UpdateValueStrategy strategyName = new UpdateValueStrategy();
        strategyName.setBeforeSetValidator(new SportlerValidator(5));

        final Binding bindName = ctx.bindValue(widgetValue, modelValue, strategyName, null);
        // Add some decorations
        ControlDecorationSupport.create(bindName, SWT.TOP | SWT.RIGHT);

        // Bind the age including a validator
        widgetValue = WidgetProperties.text(SWT.Modify).observe(ageTf);
        modelValue = BeanProperties.value(Sportler.class, "age").observe(sportler); //$NON-NLS-1$
        // Add an validator so that age can only be a number
        final UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setBeforeSetValidator(new NumberValidator(12, 99, Messages.CreateAthleteView19));
        // strategy.setBeforeSetValidator(validator);

        final Binding bindValue = ctx.bindValue(widgetValue, modelValue, strategy, null);
        // Add some decorations
        ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.RIGHT);

        // pulse
        widgetValue = WidgetProperties.text(SWT.Modify).observe(pulseTf);
        modelValue = BeanProperties.value(Sportler.class, "maxHeartBeat").observe(sportler); //$NON-NLS-1$
        // Add an validator so that age can only be a number

        final UpdateValueStrategy strategyPulse = new UpdateValueStrategy();
        strategyPulse.setBeforeSetValidator(new NumberValidator(150, 220, Messages.CreateAthleteView20));

        final Binding bindMaxPulse = ctx.bindValue(widgetValue, modelValue, strategyPulse, null);
        // Add some decorations
        ControlDecorationSupport.create(bindMaxPulse, SWT.TOP | SWT.RIGHT);
        // -----------------------

        final IObservableValue errorObservable = WidgetProperties.text().observe(errorLabel);
        // This one listenes to all changes
        ctx.bindValue(errorObservable, new AggregateValidationStatus(ctx.getBindings(), AggregateValidationStatus.MAX_SEVERITY), null, null);

        errorObservable.addChangeListener(new IChangeListener() {

            @Override
            public void handleChange(final ChangeEvent event) {
                if (ValidationStatus.OK_STATUS.getMessage().equals(errorLabel.getText())) {
                    createButton.setEnabled(true);
                } else {
                    createButton.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

}
