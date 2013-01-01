package ch.opentrainingcenter.client.views.dialoge;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.MultiValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.cache.StreckeCache;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;

public class RouteDialog extends TitleAreaDialog {

    private static final Logger LOG = Logger.getLogger(RouteDialog.class);

    private final StreckeModel model;

    private DataBindingContext ctx;
    private Text name;
    private Text beschreibung;

    private final IDatabaseAccess databaseAccess;

    private final Shell parent;

    public RouteDialog(final Shell parent, final IDatabaseAccess databaseAccess, final IAthlete athlete) {
        super(parent);
        this.parent = parent;
        this.databaseAccess = databaseAccess;
        model = new StreckeModel(athlete);
    }

    @Override
    protected Control createDialogArea(final Composite composite) {
        setTitle("Neue Strecke");
        setMessage("Eine neue Strecke erfassen um verschiedene Läufe,\nderselben Strecke miteinander zu vergleichen.");
        setTitleImage(Activator.getImageDescriptor(IImageKeys.ROUTE6464).createImage());

        final Composite container = new Composite(composite, SWT.NONE);

        final GridLayout layoutContainer = new GridLayout(2, false);
        layoutContainer.marginLeft = 10;
        layoutContainer.marginTop = 10;
        layoutContainer.horizontalSpacing = 25;
        container.setLayout(layoutContainer);
        container.setLayoutData(new GridData(5, 5, true, true, 1, 1));

        final Label labelName = new Label(container, SWT.NONE);
        labelName.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        labelName.setText("Name");

        name = new Text(container, SWT.BORDER);
        final GridData gd_pulsText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_pulsText.widthHint = 80;
        name.setLayoutData(gd_pulsText);

        final Label labelBeschreibung = new Label(container, SWT.NONE);
        labelBeschreibung.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        labelBeschreibung.setText("Beschreibung");

        beschreibung = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        final GridData noteGd = new GridData();
        noteGd.grabExcessHorizontalSpace = true;
        noteGd.grabExcessVerticalSpace = true;
        noteGd.minimumHeight = 80;
        noteGd.minimumWidth = 400;
        beschreibung.setLayoutData(noteGd);

        ctx = new DataBindingContext();
        initDataBindings();

        return container;
    }

    @Override
    protected void buttonPressed(final int buttonId) {
        if (IDialogConstants.OK_ID == buttonId) {
            // speichern
            LOG.debug("Neue Route speichern"); //$NON-NLS-1$

            IRoute route = databaseAccess.getRoute(model.getName(), model.getAthlete());
            boolean confirm = true;
            if (route != null) {
                confirm = MessageDialog.openConfirm(parent, "Es ist bereits eine Route unter diesem Namen erfasst", Messages.HealthDialog_1);
            }
            if (confirm) {
                route = CommonTransferFactory.createRoute(model.getName(), model.getBeschreibung(), model.getAthlete());
                databaseAccess.saveOrUpdate(route);
                StreckeCache.getInstance().add(model);
                super.buttonPressed(buttonId);
            }
        } else {
            super.buttonPressed(buttonId);
        }
    }

    @Override
    protected Control createButtonBar(final Composite p) {
        final Control c = super.createButtonBar(p);
        final Button ok = getButton(OK);
        ok.setEnabled(false);
        return c;
    }

    private void initDataBindings() {
        // -- Name -------------------
        final IObservableValue textNameObservable = SWTObservables.observeText(name, SWT.Modify);
        final IObservableValue modelNameObservable = BeansObservables.observeValue(model, "name"); //$NON-NLS-1$
        ctx.bindValue(textNameObservable, modelNameObservable, null, null);

        // -- Beschreibung -------------------
        final IObservableValue textBeschreibungNameObservable = SWTObservables.observeText(beschreibung, SWT.Modify);
        final IObservableValue modelBeschreibungObservable = BeansObservables.observeValue(model, "beschreibung"); //$NON-NLS-1$
        ctx.bindValue(textBeschreibungNameObservable, modelBeschreibungObservable, null, null);

        final MultiValidator multi = new MultiValidator() {
            @Override
            protected IStatus validate() {
                boolean nameValid = false;
                final Object nameVal = textNameObservable.getValue();

                final String nameVonModel = nameVal.toString();
                if (nameVonModel != null && nameVonModel.length() > 3) {
                    nameValid = true;
                }

                if (nameValid) {
                    getButton(OK).setEnabled(true);
                    setErrorMessage(null);
                    return ValidationStatus.ok();
                } else {
                    getButton(OK).setEnabled(false);
                    setErrorMessage("Name der Strecke muss eingegeben sein");
                    return ValidationStatus.error("Mist"); //$NON-NLS-1$
                }
            }
        };

        ctx.addValidationStatusProvider(multi);

        ctx.bindValue(multi.observeValidatedValue(textNameObservable), modelNameObservable);
    }
}
