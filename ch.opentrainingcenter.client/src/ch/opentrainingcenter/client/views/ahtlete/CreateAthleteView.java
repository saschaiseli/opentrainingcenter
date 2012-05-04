package ch.opentrainingcenter.client.views.ahtlete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
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
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.model.sportler.Sportler;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.importer.ImportJob;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;

public class CreateAthleteView extends ViewPart {
    public static final String ID = "ch.opentrainingcenter.client.athlete.createathlete"; //$NON-NLS-1$
    private static final Logger LOGGER = Logger.getLogger(CreateAthleteView.class);
    public static final String IMAGE = "icons/create.png"; //$NON-NLS-1$
    private final Sportler sportler = new Sportler();
    private Text nameTf;
    private Text ageTf;
    private Text pulseTf;
    private Combo genderCombo;
    private Label errorLabel;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;
    private Combo user;
    private Section selectSportler;
    private Section overviewSection;
    private final Map<Integer, Integer> indexOfSelectBoxMappedToDatabaseId = new HashMap<Integer, Integer>();

    public CreateAthleteView() {
    }

    @Override
    public void createPartControl(final Composite parent) {

        LOGGER.debug("create single activity view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        // form.setSize(1000, 2000);
        // gridlayout definieren

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        final Composite body = form.getBody();
        body.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        body.setLayoutData(td);
        form.setText(Messages.CreateAthleteView_0);

        createSelectSportler(body);
        createAddSportlerMask(body);
    }

    private void createSelectSportler(final Composite body) {
        selectSportler = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        selectSportler.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        selectSportler.setExpanded(false);
        td = new TableWrapData();
        td.colspan = 1;
        selectSportler.setLayoutData(td);
        selectSportler.setText(Messages.CreateAthleteView_1);
        selectSportler.setDescription(Messages.CreateAthleteView_2);

        final Composite sportlerComposite = toolkit.createComposite(selectSportler);
        final GridLayout layoutClient = new GridLayout(2, false);
        sportlerComposite.setLayout(layoutClient);

        final List<IAthlete> allAthletes = DatabaseAccessFactory.getDatabaseAccess().getAllAthletes();

        // gender
        final Label sportlerLabel = new Label(sportlerComposite, SWT.NONE);
        sportlerLabel.setText(Messages.CreateAthleteView_3);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = false;
        sportlerLabel.setLayoutData(gridData);
        //
        user = new Combo(sportlerComposite, SWT.NONE);
        int i = 0;
        for (final IAthlete athlete : allAthletes) {
            user.add(athlete.getName(), i);
            indexOfSelectBoxMappedToDatabaseId.put(i, athlete.getId());
            i++;
        }

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = false;
        gridData.horizontalAlignment = SWT.LEFT;
        gridData.horizontalIndent = 5;
        user.setLayoutData(gridData);

        final Button selectUser = new Button(sportlerComposite, SWT.PUSH);
        selectUser.setText(Messages.CreateAthleteView_4);
        selectUser.setEnabled(false);
        selectUser.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final int selectionIndex = user.getSelectionIndex();
                final int dbId = indexOfSelectBoxMappedToDatabaseId.get(selectionIndex);
                Activator.getDefault().getPreferenceStore().setValue(PreferenceConstants.ATHLETE_ID, String.valueOf(dbId));
                final IAthlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(dbId);
                LOGGER.info(Messages.CreateAthleteView_5 + athlete + " wird im Cache gesetzt."); //$NON-NLS-1$
                TrainingCenterDataCache.getInstance().setSelectedProfile(athlete);
                final Job job = new ImportJob(Messages.CreateAthleteView_6, athlete);
                job.schedule();
                getViewSite().getWorkbenchWindow().getShell().setText(Application.WINDOW_TITLE + Messages.CreateAthleteView_7 + athlete.getName());
            }
        });

        user.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                enableOrDisableButtonIfNoUserIsSelected(selectUser);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {

            }
        });

        user.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(final FocusEvent e) {
                enableOrDisableButtonIfNoUserIsSelected(selectUser);
            }

            @Override
            public void focusGained(final FocusEvent e) {

            }
        });

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = false;
        gridData.horizontalAlignment = SWT.LEFT;
        selectUser.setLayoutData(gridData);

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

    private void createAddSportlerMask(final Composite body) {

        overviewSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        overviewSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        overviewSection.setExpanded(false);
        td = new TableWrapData();
        td.colspan = 1;
        overviewSection.setLayoutData(td);
        overviewSection.setText(Messages.CreateAthleteView_8);
        overviewSection.setDescription(Messages.CreateAthleteView_9);

        final Composite overViewComposite = toolkit.createComposite(overviewSection);
        final GridLayout layoutClient = new GridLayout(2, false);
        overViewComposite.setLayout(layoutClient);

        // name
        final Label firstLabel = new Label(overViewComposite, SWT.NONE);
        firstLabel.setText(Messages.CreateAthleteView_10);
        nameTf = new Text(overViewComposite, SWT.BORDER);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 5;
        nameTf.setLayoutData(gridData);

        // alter
        final Label ageLabel = new Label(overViewComposite, SWT.NONE);
        ageLabel.setText(Messages.CreateAthleteView_11);
        ageTf = new Text(overViewComposite, SWT.BORDER);

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 5;
        ageTf.setLayoutData(gridData);

        // pulse
        final Label pulseLabel = new Label(overViewComposite, SWT.NONE);
        pulseLabel.setText(Messages.CreateAthleteView_12);
        pulseTf = new Text(overViewComposite, SWT.BORDER);

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalIndent = 5;
        pulseTf.setLayoutData(gridData);

        // gender
        final Label genderLabel = new Label(overViewComposite, SWT.NONE);
        genderLabel.setText(Messages.CreateAthleteView_13);
        genderCombo = new Combo(overViewComposite, SWT.NONE);
        genderCombo.add(Messages.CreateAthleteView_14);
        genderCombo.add(Messages.CreateAthleteView_15);

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.RIGHT;
        gridData.grabExcessHorizontalSpace = false;
        gridData.horizontalIndent = 5;
        genderCombo.setLayoutData(gridData);

        errorLabel = new Label(overViewComposite, SWT.NONE);
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        errorLabel.setLayoutData(gridData);

        final Button button1 = new Button(overViewComposite, SWT.PUSH);
        button1.setText(Messages.CreateAthleteView_16);
        button1.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                LOGGER.info("save " + sportler); //$NON-NLS-1$
                final IAthlete athlete = CommonTransferFactory.createAthlete(sportler.getName(), sportler.getAge(), sportler.getMaxHeartBeat());
                try {
                    DatabaseAccessFactory.getDatabaseAccess().save(athlete);
                    errorLabel.setText(""); //$NON-NLS-1$
                    overviewSection.setExpanded(false);
                    selectSportler.setExpanded(true);
                } catch (final Exception e1) {
                    errorLabel.setText(Messages.CreateAthleteView_17);
                }
            }
        });
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.LEFT;
        gridData.grabExcessHorizontalSpace = false;
        gridData.horizontalIndent = 5;
        button1.setLayoutData(gridData);

        bindValues();

        overviewSection.setClient(overViewComposite);
    }

    private void enableOrDisableButtonIfNoUserIsSelected(final Button selectUser) {
        if (user.getSelectionIndex() >= 0) {
            selectUser.setEnabled(true);
        } else {
            selectUser.setToolTipText(Messages.CreateAthleteView_18);
        }
    }

    private void bindValues() {
        // The DataBindingContext object will manage the databindings
        // Lets bind it
        final DataBindingContext ctx = new DataBindingContext();

        // name
        IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(nameTf);
        IObservableValue modelValue = BeanProperties.value(Sportler.class, "name").observe(sportler); //$NON-NLS-1$
        final UpdateValueStrategy strategyName = new UpdateValueStrategy();
        strategyName.setBeforeSetValidator(new SportlerValidator(5));
        // strategy.setBeforeSetValidator(validator);

        final Binding bindName = ctx.bindValue(widgetValue, modelValue, strategyName, null);
        // Add some decorations
        ControlDecorationSupport.create(bindName, SWT.TOP | SWT.RIGHT);

        // Bind the age including a validator
        widgetValue = WidgetProperties.text(SWT.Modify).observe(ageTf);
        modelValue = BeanProperties.value(Sportler.class, "age").observe(sportler); //$NON-NLS-1$
        // Add an validator so that age can only be a number
        final UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setBeforeSetValidator(new NumberValidator(18, 99, Messages.CreateAthleteView_19));
        // strategy.setBeforeSetValidator(validator);

        final Binding bindValue = ctx.bindValue(widgetValue, modelValue, strategy, null);
        // Add some decorations
        ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.RIGHT);

        // pulse
        widgetValue = WidgetProperties.text(SWT.Modify).observe(pulseTf);
        modelValue = BeanProperties.value(Sportler.class, "maxHeartBeat").observe(sportler); //$NON-NLS-1$
        // Add an validator so that age can only be a number

        final UpdateValueStrategy strategyPulse = new UpdateValueStrategy();
        strategyPulse.setBeforeSetValidator(new NumberValidator(160, 220, Messages.CreateAthleteView_20));

        final Binding bindMaxPulse = ctx.bindValue(widgetValue, modelValue, strategyPulse, null);
        // Add some decorations
        ControlDecorationSupport.create(bindMaxPulse, SWT.TOP | SWT.RIGHT);
        // -----------------------
        //
        // gender
        widgetValue = WidgetProperties.selection().observe(genderCombo);
        modelValue = BeansObservables.observeValue(sportler, "gender"); //$NON-NLS-1$
        ctx.bindValue(widgetValue, modelValue);

        // final IObservableValue errorObservable =
        // WidgetProperties.text().observe(errorLabel);
        // // This one listenes to all changes
        // ctx.bindValue(errorObservable, new
        // AggregateValidationStatus(ctx.getBindings(),
        // AggregateValidationStatus.MAX_SEVERITY), null, null);

    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
    }
}
