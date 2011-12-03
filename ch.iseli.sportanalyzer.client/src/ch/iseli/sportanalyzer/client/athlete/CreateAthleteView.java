package ch.iseli.sportanalyzer.client.athlete;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.Application;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.helper.DaoHelper;
import ch.iseli.sportanalyzer.client.model.sportler.Sportler;
import ch.iseli.sportanalyzer.db.IImportedDao;
import ch.opentrainingcenter.transfer.impl.Athlete;

public class CreateAthleteView extends ViewPart {
    public static final String ID = "ch.iseli.sportanalyzer.client.athlete.createathlete";
    private static final Logger logger = Logger.getLogger(CreateAthleteView.class);
    public static final String IMAGE = "icons/create.png";
    private final Sportler sportler = new Sportler();
    private Text nameTf;
    private Text ageText;
    private Combo genderCombo;
    private Text pulseText;
    private Label errorLabel;
    private IImportedDao dao;

    @Override
    public void createPartControl(final Composite parent) {
        final IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor(Application.CH_OPENTRAININGDATABASE_DB);
        logger.info("daos suchen: " + daos.length);
        dao = (IImportedDao) DaoHelper.getDao(daos, IImportedDao.EXTENSION_POINT_NAME);
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_NAME);

        final GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);

        if (athleteId != null && athleteId.length() > 0) {
            final Athlete athlete = dao.getAthlete(Integer.parseInt(athleteId));
            if (athlete == null) {
                // Athlete nicht gefunden. Einen auswählen
                final Label label = new Label(parent, SWT.NONE);
                label.setText("Den Benutzer auswählen");
                final List<Athlete> allAthletes = dao.getAllAthletes();
                final ComboViewer combo = new ComboViewer(parent, SWT.READ_ONLY);
                combo.setContentProvider(new ArrayContentProvider());
                combo.setLabelProvider(new LabelProvider() {

                    @Override
                    public String getText(final Object element) {
                        final Athlete athlete = (Athlete) element;
                        return athlete.getName();
                    }

                });

                combo.setInput(allAthletes);
                combo.addSelectionChangedListener(new ISelectionChangedListener() {

                    @Override
                    public void selectionChanged(final SelectionChangedEvent event) {
                        final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                        final Athlete a = (Athlete) selection.getFirstElement();
                        Activator.getDefault().getPreferenceStore().setValue(PreferenceConstants.ATHLETE_NAME, a.getName());
                    }
                });
            }
        } else {
            createAddSportlerMask(parent);
        }
    }

    private void createAddSportlerMask(final Composite parent) {
        final Label label = new Label(parent, SWT.NONE);
        label.setText("Neuen Benutzer erstellen");
        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        label.setLayoutData(gridData);
        // name
        final Label firstLabel = new Label(parent, SWT.NONE);
        firstLabel.setText("Name: ");
        nameTf = new Text(parent, SWT.BORDER);

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        nameTf.setLayoutData(gridData);

        // alter
        final Label ageLabel = new Label(parent, SWT.NONE);
        ageLabel.setText("Alter: ");
        ageText = new Text(parent, SWT.BORDER);

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        ageText.setLayoutData(gridData);

        // pulse
        final Label pulseLabel = new Label(parent, SWT.NONE);
        pulseLabel.setText("Maximal Puls: ");
        pulseText = new Text(parent, SWT.BORDER);

        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        pulseText.setLayoutData(gridData);

        // gender
        final Label genderLabel = new Label(parent, SWT.NONE);
        genderLabel.setText("Geschlecht: ");
        genderCombo = new Combo(parent, SWT.NONE);
        genderCombo.add("Männlich");
        genderCombo.add("Weiblich");

        final Button button1 = new Button(parent, SWT.PUSH);
        button1.setText("Speichern");
        button1.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                System.out.println("save " + sportler);
                dao.save(new Athlete(sportler.getName(), sportler.getAge(), sportler.getMaxHeartBeat(), null, null));
            }
        });

        // This label will display all errors of all bindings
        final Label descAllLabel = new Label(parent, SWT.NONE);
        descAllLabel.setText("");
        errorLabel = new Label(parent, SWT.NONE);
        gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 1;
        errorLabel.setLayoutData(gridData);

        bindValues();
    }

    private void bindValues() {
        // The DataBindingContext object will manage the databindings
        // Lets bind it
        final DataBindingContext ctx = new DataBindingContext();
        IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(nameTf);
        IObservableValue modelValue = BeanProperties.value(Sportler.class, "name").observe(sportler);
        ctx.bindValue(widgetValue, modelValue);

        // Bind the age including a validator
        widgetValue = WidgetProperties.text(SWT.Modify).observe(ageText);
        modelValue = BeanProperties.value(Sportler.class, "age").observe(sportler);

        final UpdateValueStrategy strategyAge = new UpdateValueStrategy();
        strategyAge.setBeforeSetValidator(new NumberValidator(18, 120));

        final Binding bindValue = ctx.bindValue(widgetValue, modelValue, strategyAge, null);
        // Add some decorations
        ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

        widgetValue = WidgetProperties.text(SWT.Modify).observe(pulseText);
        modelValue = BeanProperties.value(Sportler.class, "maxHeartBeat").observe(sportler);
        // Add an validator so that age can only be a number

        final UpdateValueStrategy strategyPulse = new UpdateValueStrategy();
        strategyPulse.setBeforeSetValidator(new NumberValidator(160, 220));

        final Binding bindMaxPulse = ctx.bindValue(widgetValue, modelValue, strategyPulse, null);
        // Add some decorations
        ControlDecorationSupport.create(bindMaxPulse, SWT.TOP | SWT.LEFT);

        widgetValue = WidgetProperties.selection().observe(genderCombo);
        modelValue = BeansObservables.observeValue(sportler, "gender");

        ctx.bindValue(widgetValue, modelValue);

        final IObservableValue errorObservable = WidgetProperties.text().observe(errorLabel);
        // This one listenes to all changes
        ctx.bindValue(errorObservable, new AggregateValidationStatus(ctx.getBindings(), AggregateValidationStatus.MAX_SEVERITY), null, null);
    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }
}
