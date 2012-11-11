package ch.opentrainingcenter.client.views.planung;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.planing.IPlanungModel;

public class KWViewPart {

    private DataBindingContext ctx;
    private IPlanungModel model;
    private Label kw;
    private Button buttonInterval;
    private Composite composite;
    private Scale scale;
    private Label kmProWoche;
    private Spinner langerLaufSpinner;

    public Composite addLabelAndValue(final Composite parent, final IPlanungModel m) {

        this.model = m;
        final String label = Messages.KWViewPart_0 + model.getKw();
        final int value = model.getKmProWoche();
        final boolean interval = model.isInterval();

        composite = new Composite(parent, SWT.NONE);
        // c.setBackground(getViewSite().getWorkbenchWindow().getShell().getDisplay().getSystemColor(SWT.COLOR_CYAN));
        final GridLayout layout = new GridLayout(6, false);
        composite.setLayout(layout);

        final GridData gd = new GridData();
        gd.horizontalIndent = 10;
        gd.verticalIndent = 40;
        composite.setLayoutData(gd);

        // Label
        final GridData laGd = new GridData();
        kw = new Label(composite, SWT.NONE);
        kw.setText(label);
        laGd.horizontalIndent = 10;
        kw.setLayoutData(laGd);

        scale = new Scale(composite, SWT.BORDER);
        scale.setMaximum(60);
        scale.setMinimum(0);
        scale.setIncrement(1);
        scale.setPageIncrement(5);
        scale.setSelection(value);
        final GridData gd1 = new GridData();
        gd1.minimumWidth = 400;
        gd1.grabExcessHorizontalSpace = true;
        scale.setLayoutData(gd1);

        kmProWoche = new Label(composite, SWT.NONE);
        kmProWoche.setText(value + Messages.KWViewPart_1);
        scale.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                final int newValue = scale.getSelection();
                kmProWoche.setText(newValue + Messages.KWViewPart_2);
                model.setKmProWoche(newValue);
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

        buttonInterval = new Button(composite, SWT.CHECK);
        buttonInterval.setText(Messages.KWViewPart_3);
        buttonInterval.setLayoutData(gdButton);
        buttonInterval.setSelection(interval);

        buttonInterval.addSelectionListener(new SelectionListener() {
            boolean toggle = buttonInterval.getSelection();

            @Override
            public void widgetSelected(final SelectionEvent e) {
                toggle = !toggle;
                model.setInterval(toggle);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {

            }
        });

        final Label laufLabel = new Label(composite, SWT.NONE);
        laufLabel.setText("Langer Lauf: ");
        final GridData gdLabelSpinner = new GridData();
        gdLabelSpinner.horizontalAlignment = SWT.RIGHT;
        gdLabelSpinner.horizontalIndent = 30;
        laufLabel.setLayoutData(gdLabelSpinner);

        final GridData gdSpinner = new GridData();
        gdSpinner.widthHint = 20;
        gdSpinner.horizontalIndent = 0;

        langerLaufSpinner = new Spinner(composite, SWT.BORDER);
        langerLaufSpinner.setToolTipText("Langer Lauf");
        langerLaufSpinner.setMinimum(0);
        langerLaufSpinner.setMaximum(1000);
        langerLaufSpinner.setSelection(m.getLangerLauf());
        langerLaufSpinner.setIncrement(1);
        langerLaufSpinner.setLayoutData(gdSpinner);

        ctx = new DataBindingContext();
        initDataBindings();
        return composite;
    }

    private void initDataBindings() {
        final IObservableValue intervalObservable = SWTObservables.observeImage(buttonInterval);
        final IObservableValue modelIntervalObservable = BeansObservables.observeValue(model, "interval"); //$NON-NLS-1$
        ctx.bindValue(intervalObservable, modelIntervalObservable, null, null);

        final IObservableValue langerLaufObservable = SWTObservables.observeSelection(langerLaufSpinner);
        final IObservableValue modelLangerLaufObservable = BeansObservables.observeValue(model, "langerLauf"); //$NON-NLS-1$
        ctx.bindValue(langerLaufObservable, modelLangerLaufObservable, null, null);
    }
}
