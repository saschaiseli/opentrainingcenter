package ch.opentrainingcenter.client.views.dialoge;

import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.model.sportler.HealthModel;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.client.views.ahtlete.NumberValidator;

/**
 * Dialog wo tägliche Daten, wie Gewicht und Ruhepuls erfasst werden können.
 * 
 * @author sascha
 * 
 */
public class HealthDialog extends TitleAreaDialog {

    private final HealthModel model = new HealthModel();

    public HealthDialog(final Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setMessage(Messages.HealthDialog0);
        setTitle(Messages.HealthDialog1);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.CARDIO).createImage());

        final DataBindingContext ctx = new DataBindingContext();

        final Composite c = new Composite(parent, SWT.NONE);

        final GridLayout layout = new GridLayout(2, true);
        layout.marginLeft = 100;
        layout.marginTop = 10;
        layout.horizontalSpacing = 25;
        final GridData gd = new GridData(5, 5, true, true, 1, 1);
        c.setLayout(layout);
        c.setLayoutData(gd);

        final Label ruhePuls = new Label(c, SWT.NONE);
        ruhePuls.setText(Messages.HealthDialog2);

        final Text puls = new Text(c, SWT.BORDER);
        final GridData gd_puls = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_puls.widthHint = 80;
        puls.setLayoutData(gd_puls);

        IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(puls);
        IObservableValue pulsValue = BeanProperties.value(HealthModel.class, "ruhePuls").observe(model); //$NON-NLS-1$
        final UpdateValueStrategy updatePulse = new UpdateValueStrategy();
        final NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());

        // updatePulse.setBeforeSetValidator(new
        // NumberToIntegerConverter(numberFormat, Object.class, true));// new
        // NumberValidator(10,
        // 100,
        // "Ruhepuls muss angegeben werden"));

        Binding bindName = ctx.bindValue(widgetValue, pulsValue, updatePulse, null);
        // Add some decorations
        ControlDecorationSupport.create(bindName, SWT.TOP | SWT.RIGHT);

        final Label gewicht = new Label(c, SWT.NONE);
        gewicht.setText(Messages.HealthDialog3);

        final Text gewichtValue = new Text(c, SWT.BORDER);
        final GridData gd_gewichtValue = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_gewichtValue.widthHint = 80;
        gewichtValue.setLayoutData(gd_gewichtValue);

        widgetValue = WidgetProperties.text(SWT.Modify).observe(gewichtValue);
        pulsValue = BeanProperties.value(HealthModel.class, "weight").observe(model); //$NON-NLS-1$
        final UpdateValueStrategy updateGewicht = new UpdateValueStrategy();
        updateGewicht.setBeforeSetValidator(new NumberValidator(50, 150, Messages.HealthDialog4));

        bindName = ctx.bindValue(widgetValue, pulsValue, updateGewicht, null);
        // createButtonBar(parent);

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
}
