package ch.opentrainingcenter.client.views.dialoge;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
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
import ch.opentrainingcenter.client.model.sportler.HealthModel;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.client.views.databinding.NumberValidator;
import ch.opentrainingcenter.client.views.databinding.StringToDoubleConverter;
import ch.opentrainingcenter.client.views.databinding.StringToIntegerConverter;

/**
 * Dialog wo tägliche Daten, wie Gewicht und Ruhepuls erfasst werden können.
 * 
 * @author sascha
 * 
 */
public class HealthDialog extends TitleAreaDialog {

    private final HealthModel model = new HealthModel();
    private Text pulsText;
    private Text gewichtText;

    private DataBindingContext ctx;

    public HealthDialog(final Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setMessage(Messages.HealthDialog0);
        setTitle(Messages.HealthDialog1);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.CARDIO3232).createImage());

        final Composite c = new Composite(parent, SWT.NONE);

        final GridLayout layout = new GridLayout(2, true);
        layout.marginLeft = 100;
        layout.marginTop = 10;
        layout.horizontalSpacing = 25;
        final GridData gd = new GridData(5, 5, true, true, 1, 1);
        c.setLayout(layout);
        c.setLayoutData(gd);

        final Label lblDatum = new Label(c, SWT.NONE);
        lblDatum.setText("Datum");

        final DateTime dateTime = new DateTime(c, SWT.BORDER | SWT.CALENDAR);

        final Label ruhePuls = new Label(c, SWT.NONE);
        ruhePuls.setText(Messages.HealthDialog2);

        pulsText = new Text(c, SWT.BORDER);
        final GridData gd_pulsText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_pulsText.widthHint = 80;
        pulsText.setLayoutData(gd_pulsText);

        final Label gewicht = new Label(c, SWT.NONE);
        gewicht.setText(Messages.HealthDialog3);

        gewichtText = new Text(c, SWT.BORDER);

        final GridData gd_gewichtText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_gewichtText.widthHint = 80;
        gewichtText.setLayoutData(gd_gewichtText);
        ctx = new DataBindingContext();
        initDataBindings();

        ctx.getBindings().addChangeListener(new IChangeListener() {

            @Override
            public void handleChange(final ChangeEvent event) {
                boolean enable = true;
                for (final Object o : ctx.getBindings()) {
                    final Binding binding = (Binding) o;
                    final IStatus status = (IStatus) binding.getValidationStatus().getValue();
                    if (ValidationStatus.OK_STATUS.equals(status)) {
                        continue;
                    } else {
                        enable = false;
                        // break;
                    }
                }
                getButton(OK).setEnabled(enable);
            }
        });

        // final AggregateValidationStatus aggr = new
        // AggregateValidationStatus(ctx.getBindings(),
        // AggregateValidationStatus.MAX_SEVERITY);
        // aggr.addChangeListener(new IChangeListener() {
        //
        // @Override
        // public void handleChange(final ChangeEvent event) {
        // boolean enable = true;
        // for (final Object o : ctx.getBindings()) {
        // final Binding binding = (Binding) o;
        // final IStatus status = (IStatus)
        // binding.getValidationStatus().getValue();
        // if (ValidationStatus.OK_STATUS.equals(status)) {
        // continue;
        // } else {
        // enable = false;
        // // break;
        // }
        // }
        // getButton(OK).setEnabled(enable);
        // }
        // });
        return c;
    }

    @Override
    protected void buttonPressed(final int buttonId) {
        if (IDialogConstants.OK_ID == buttonId) {
            // puls und gewicht speichern
            System.out.println(Messages.HealthDialog5);
        }
        super.buttonPressed(buttonId);
    }

    @Override
    protected Control createButtonBar(final Composite parent) {
        final Control c = super.createButtonBar(parent);
        getButton(OK).setEnabled(false);
        return c;
    }

    protected void initDataBindings() {

        final String errorMessage = getErrorMessage();
        // setErrorMessage("huhuuuu");

        // -- Gewicht -------------------
        final IObservableValue pulsTextObserveTextObserveWidget = SWTObservables.observeText(pulsText, SWT.Modify);
        final IObservableValue modelRuhePulsObserveValue = BeansObservables.observeValue(model, "ruhePuls"); //$NON-NLS-1$
        // strategy
        final UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setBeforeSetValidator(new NumberValidator(10, 100, "Bitte Ruhepuls eingeben"));
        strategy.setConverter(new StringToIntegerConverter());

        final Binding bindValue = ctx.bindValue(pulsTextObserveTextObserveWidget, modelRuhePulsObserveValue, strategy, null);
        ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.RIGHT);

        // -- Puls -------------------
        final IObservableValue gewichtTextObserveTextObserveWidget = SWTObservables.observeText(gewichtText, SWT.Modify);
        final IObservableValue modelWeightObserveValue = BeansObservables.observeValue(model, "weight"); //$NON-NLS-1$

        // strategy
        final UpdateValueStrategy strategyGewicht = new UpdateValueStrategy();
        strategyGewicht.setBeforeSetValidator(new NumberValidator(10, 100, "Bitte Gewicht eingeben"));
        strategyGewicht.setConverter(new StringToDoubleConverter());

        final Binding bindGewicht = ctx.bindValue(gewichtTextObserveTextObserveWidget, modelWeightObserveValue, strategyGewicht, null);
        ControlDecorationSupport.create(bindGewicht, SWT.TOP | SWT.RIGHT);
    }
}
