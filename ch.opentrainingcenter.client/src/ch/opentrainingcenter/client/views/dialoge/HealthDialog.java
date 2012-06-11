package ch.opentrainingcenter.client.views.dialoge;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.model.sportler.HealthModel;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.client.views.databinding.NumberValidator;
import ch.opentrainingcenter.client.views.databinding.StringToDoubleConverter;
import ch.opentrainingcenter.client.views.databinding.StringToIntegerConverter;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;

/**
 * Dialog wo tägliche Daten, wie Gewicht und Ruhepuls erfasst werden können.
 * 
 * @author sascha
 * 
 */
public class HealthDialog extends TitleAreaDialog {

    private static final Logger LOG = Logger.getLogger(HealthDialog.class);

    private final HealthModel model = new HealthModel();
    private Text pulsText;
    private Text gewichtText;

    private DataBindingContext ctx;
    private Label errorLabel;

    private DateTime dateTime;

    private final IDatabaseAccess db;

    private final IAthlete athlete;

    public HealthDialog(final Shell parentShell) {
        this(parentShell, DatabaseAccessFactory.getDatabaseAccess(), Activator.getDefault().getPreferenceStore());
    }

    public HealthDialog(final Shell parentShell, final IDatabaseAccess databaseAccess, final IPreferenceStore store) {
        super(parentShell);
        this.db = databaseAccess;
        final String id = store.getString(PreferenceConstants.ATHLETE_ID);
        athlete = databaseAccess.getAthlete(Integer.valueOf(id));
        Assert.isNotNull(athlete);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setMessage(Messages.HealthDialog0);
        setTitle(Messages.HealthDialog1);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.CARDIO3232).createImage());

        final Composite c = new Composite(parent, SWT.NONE);

        final GridLayout layout = new GridLayout(2, true);
        layout.marginHeight = 10;
        layout.marginRight = 10;
        layout.marginLeft = 10;
        layout.marginTop = 10;
        layout.horizontalSpacing = 10;
        final GridData gd = new GridData(5, 5, true, true, 1, 1);
        c.setLayout(layout);
        c.setLayoutData(gd);

        dateTime = new DateTime(c, SWT.BORDER | SWT.CALENDAR);
        dateTime.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

        final Composite containerTextFields = new Composite(c, SWT.NONE);
        final GridLayout layoutContainer = new GridLayout(2, true);
        layoutContainer.marginLeft = 10;
        layoutContainer.marginTop = 10;
        layoutContainer.horizontalSpacing = 25;
        containerTextFields.setLayout(layoutContainer);
        containerTextFields.setLayoutData(new GridData(5, 5, true, true, 1, 1));

        final Label ruhePuls = new Label(containerTextFields, SWT.NONE);
        ruhePuls.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        ruhePuls.setText(Messages.HealthDialog2);

        pulsText = new Text(containerTextFields, SWT.BORDER);
        pulsText.setText("");
        final GridData gd_pulsText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_pulsText.widthHint = 80;
        pulsText.setLayoutData(gd_pulsText);

        final Label gewicht = new Label(containerTextFields, SWT.NONE);
        gewicht.setText(Messages.HealthDialog3);

        gewichtText = new Text(containerTextFields, SWT.BORDER);

        final GridData gd_gewichtText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_gewichtText.widthHint = 80;
        gewichtText.setLayoutData(gd_gewichtText);
        new Label(c, SWT.NONE);
        new Label(c, SWT.NONE);
        new Label(c, SWT.NONE);

        errorLabel = new Label(c, SWT.NONE);
        errorLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        errorLabel.setVisible(true);
        new Label(c, SWT.NONE);
        new Label(c, SWT.NONE);

        ctx = new DataBindingContext();
        initDataBindings();

        return c;
    }

    @Override
    protected void buttonPressed(final int buttonId) {
        if (IDialogConstants.OK_ID == buttonId) {
            model.setDateOfMeasure((Date) dateTime.getData());
            db.saveHealth(CommonTransferFactory.createHealth(athlete, model.getWeight(), model.getRuhePuls(), model.getDateOfMeasure()));
        }
        super.buttonPressed(buttonId);
    }

    @Override
    protected Control createButtonBar(final Composite parent) {
        final Control c = super.createButtonBar(parent);
        // getButton(OK).setEnabled(false);
        return c;
    }

    protected void initDataBindings() {

        // -- puls -------------------
        final IObservableValue pulseObservable = SWTObservables.observeText(pulsText, SWT.Modify);
        final IObservableValue modelRuhePulsObserveValue = BeansObservables.observeValue(model, "ruhePuls"); //$NON-NLS-1$
        // strategy
        final UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setBeforeSetValidator(new NumberValidator(10, 100, "Bitte Ruhepuls eingeben"));
        strategy.setConverter(new StringToIntegerConverter());

        final Binding bindValue = ctx.bindValue(pulseObservable, modelRuhePulsObserveValue, strategy, null);
        ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.RIGHT);

        // -- gewicht -------------------
        final IObservableValue gewichtObservable = SWTObservables.observeText(gewichtText, SWT.Modify);
        final IObservableValue modelWeightObserveValue = BeansObservables.observeValue(model, "weight"); //$NON-NLS-1$

        // strategy
        final UpdateValueStrategy strategyGewicht = new UpdateValueStrategy();
        strategyGewicht.setBeforeSetValidator(new NumberValidator(10, 100, "Bitte Gewicht eingeben"));
        strategyGewicht.setConverter(new StringToDoubleConverter());

        final Binding bindGewicht = ctx.bindValue(gewichtObservable, modelWeightObserveValue, strategyGewicht, null);
        ControlDecorationSupport.create(bindGewicht, SWT.TOP | SWT.RIGHT);

        //
        // final IObservableValue errorObservable =
        // WidgetProperties.text().observe(errorLabel);
        // // This one listenes to all changes
        // final AggregateValidationStatus aggrStatus = new
        // AggregateValidationStatus(ctx.getBindings(),
        // AggregateValidationStatus.MAX_SEVERITY);
        //
        // ctx.bindValue(errorObservable, aggrStatus, null, null);

        final PropertyChangeSupport pcs = model.getPropertyChangeSupport();
        pcs.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent arg0) {
                System.out.println("property changed");
            }
        });

        final IChangeListener listener = new IChangeListener() {

            @Override
            public void handleChange(final ChangeEvent event) {
                System.out.println("Textfield changed: ");
                final IObservableList stats = ctx.getValidationStatusProviders();

                final IStatus status = AggregateValidationStatus.getStatusMaxSeverity(stats);
                boolean enable = false;
                if (status.isMultiStatus()) {
                    LOG.info("Multistatus");
                    final MultiStatus multi = (MultiStatus) status;
                    LOG.info("Anzahl status childs: " + multi.getMessage() + " " + multi.getChildren().length);
                    if (multi.getChildren().length == 1) {
                        LOG.info("Nur ein child, somit ist mind eines ok ");
                        enable = true;
                    }
                } else {
                    LOG.info("Singlestatus mit wert: " + status.isOK());
                    enable = status.isOK();
                }
                // if (getButton(OK) != null) {
                // getButton(OK).setEnabled(enable);
                // }
            }
        };
        pulseObservable.addChangeListener(listener);
        gewichtObservable.addChangeListener(listener);
    }
}
