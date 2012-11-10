package ch.opentrainingcenter.client.views.dialoge;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.conversion.StringToNumberConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.MultiValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.cache.impl.HealthCache;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteHealth;
import ch.opentrainingcenter.client.model.sportler.HealthModel;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.client.views.databinding.NumberValidator;
import ch.opentrainingcenter.client.views.databinding.StringToIntegerConverter;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;

import com.ibm.icu.text.NumberFormat;

/**
 * Dialog wo tägliche Daten, wie Gewicht und Ruhepuls erfasst werden können.
 * 
 * @author sascha
 * 
 */
public class HealthDialog extends TitleAreaDialog {

    private static final Logger LOG = Logger.getLogger(HealthDialog.class);

    private final HealthModel model;
    private Text pulsText;
    private Text gewichtText;

    private DataBindingContext ctx;
    private Label errorLabel;

    private DateTime dateTime;

    private final Shell parent;
    private final IDatabaseAccess db;
    private final IAthlete athlete;

    private final Date date;

    public HealthDialog(final Shell parent) {
        this(parent, DatabaseAccessFactory.getDatabaseAccess(), Activator.getDefault().getPreferenceStore(), new Date());
    }

    public HealthDialog(final Shell parent, final IHealth health) {
        this(parent, DatabaseAccessFactory.getDatabaseAccess(), Activator.getDefault().getPreferenceStore(), health.getDateofmeasure());
    }

    public HealthDialog(final Shell parent, final IDatabaseAccess databaseAccess, final IPreferenceStore store, final Date date) {
        super(parent);
        this.parent = parent;
        this.db = databaseAccess;
        this.date = date;
        final String id = store.getString(PreferenceConstants.ATHLETE_ID);
        athlete = db.getAthlete(Integer.valueOf(id));
        final IHealth healt = db.getHealth(athlete, date);
        if (healt != null) {
            model = new HealthModel(healt.getWeight(), healt.getCardio(), healt.getDateofmeasure());
        } else {
            model = new HealthModel();
        }
        Assert.isNotNull(athlete);
    }

    @Override
    protected Control createDialogArea(final Composite composite) {
        setMessage(Messages.HealthDialog0);
        setTitle(Messages.HealthDialog1);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.CARDIO3232).createImage());

        final Composite c = new Composite(composite, SWT.NONE);

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

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        dateTime.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
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
        final GridData gd_pulsText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_pulsText.widthHint = 80;
        pulsText.setLayoutData(gd_pulsText);

        final Label gewichtLabel = new Label(containerTextFields, SWT.NONE);
        gewichtLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        gewichtLabel.setText(Messages.HealthDialog3);

        gewichtText = new Text(containerTextFields, SWT.BORDER);
        final GridData gd_gewichtText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_gewichtText.widthHint = 80;
        gewichtText.setLayoutData(gd_gewichtText);

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
            final Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.set(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
            final Date dateOfMeasure = cal.getTime();
            model.setDateOfMeasure(dateOfMeasure);
            final IHealth health = db.getHealth(athlete, dateOfMeasure);
            boolean confirm = true;
            if (health != null) {
                confirm = MessageDialog.openConfirm(parent, Messages.HealthDialog_0, Messages.HealthDialog_1);
            }
            if (confirm) {
                final IHealth healthToSave = CommonTransferFactory.createHealth(athlete, model.getWeight(), model.getRuhePuls(), model.getDateOfMeasure());
                final int id = db.saveOrUpdate(healthToSave);
                healthToSave.setId(id);
                HealthCache.getInstance().add(new ConcreteHealth(healthToSave));
            }
        } else {
            super.buttonPressed(buttonId);
        }

    }

    @Override
    protected Control createButtonBar(final Composite p) {
        final Control c = super.createButtonBar(p);
        final Button ok = getButton(OK);
        ok.setEnabled(model.getDateOfMeasure() != null);
        return c;
    }

    protected void initDataBindings() {

        // -- puls -------------------
        final IObservableValue textPulsObservable = SWTObservables.observeText(pulsText, SWT.Modify);
        final IObservableValue modelPulsObservable = BeansObservables.observeValue(model, "ruhePuls"); //$NON-NLS-1$
        // strategy
        final UpdateValueStrategy strategyPuls = new UpdateValueStrategy(UpdateValueStrategy.POLICY_CONVERT);
        strategyPuls.setConverter(new StringToIntegerConverter());

        ctx.bindValue(textPulsObservable, modelPulsObservable, strategyPuls, null);

        //
        // ----------------------------------------------------
        // --------------- gewicht ----------------------------
        // ----------------------------------------------------
        //
        final IObservableValue textGewichtObservable = SWTObservables.observeText(gewichtText, SWT.Modify);
        final IObservableValue modelGewichtObservable = BeansObservables.observeValue(model, "weight"); //$NON-NLS-1$
        // strategy
        final UpdateValueStrategy strategyGewicht = new UpdateValueStrategy();
        strategyGewicht.setAfterGetValidator(new NumberValidator(20.0, Double.MAX_VALUE, "")); //$NON-NLS-1$

        final NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        final IConverter numberToStringConverter = StringToNumberConverter.toDouble(numberFormat, false);
        strategyGewicht.setConverter(numberToStringConverter);

        ctx.bindValue(textGewichtObservable, modelGewichtObservable, strategyGewicht, null);

        final MultiValidator multi = new MultiValidator() {
            @Override
            protected IStatus validate() {
                LOG.debug("validate"); //$NON-NLS-1$
                boolean pulsValid = false;
                boolean gewichtValid = false;
                final Object pulsVal = textPulsObservable.getValue();
                final Object gewichtVal = textGewichtObservable.getValue();
                try {
                    final Integer puls = Integer.valueOf(pulsVal.toString());
                    pulsValid = pulsValid(puls);
                } catch (final NumberFormatException nfe) {

                }

                try {
                    final Double gewicht = Double.valueOf(gewichtVal.toString());
                    gewichtValid = gewichtValid(gewicht);
                } catch (final NumberFormatException nfe) {

                }

                if (pulsValid || gewichtValid) {
                    getButton(OK).setEnabled(true);
                    setErrorMessage(null);
                    return ValidationStatus.ok();
                } else {
                    getButton(OK).setEnabled(false);
                    setErrorMessage(Messages.HealthDialog_3);
                    return ValidationStatus.error("Mist"); //$NON-NLS-1$
                }
            }

            private boolean gewichtValid(final Double gewicht) {
                return gewicht != null && gewicht.doubleValue() > 0;
            }

            private boolean pulsValid(final Integer puls) {
                return puls != null && puls > 0;
            }
        };

        ctx.addValidationStatusProvider(multi);

        ctx.bindValue(multi.observeValidatedValue(textPulsObservable), modelPulsObservable);
        ctx.bindValue(multi.observeValidatedValue(textGewichtObservable), modelGewichtObservable);
    }
}
