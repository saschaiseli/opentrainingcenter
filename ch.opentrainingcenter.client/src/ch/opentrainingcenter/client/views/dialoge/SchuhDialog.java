package ch.opentrainingcenter.client.views.dialoge;

import java.util.List;

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
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.cache.SchuhCache;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.schuh.SchuhModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class SchuhDialog extends TitleAreaDialog {

    private static final Logger LOG = Logger.getLogger(SchuhDialog.class);

    private final IDatabaseAccess databaseAccess;
    private Text schuhName;

    private DataBindingContext ctx;

    private final SchuhModel model;

    private final Shell parent;

    public SchuhDialog(final Shell parent, final IDatabaseAccess databaseAccess, final IAthlete athlete) {
        super(parent);
        this.parent = parent;
        this.databaseAccess = databaseAccess;
        model = new SchuhModel(athlete);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setTitle(Messages.SchuhDialog_Add_Schuh);
        setMessage(Messages.SchuhDialog_Beschreibung);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.ROUTE6464).createImage());

        final Composite container = new Composite(parent, SWT.NONE);

        GridLayoutFactory.fillDefaults().numColumns(2).margins(20, 0).applyTo(container);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container);

        // ---
        final Label labelName = new Label(container, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, true).applyTo(labelName);
        labelName.setText(Messages.SchuhDialog_Label_Name_Marke);

        schuhName = new Text(container, SWT.BORDER);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).applyTo(schuhName);

        ctx = new DataBindingContext();
        initDataBindings();

        return container;
    }

    private void initDataBindings() {
        // -- Name -------------------
        final IObservableValue textNameObservable = SWTObservables.observeText(schuhName, SWT.Modify);
        final IObservableValue modelNameObservable = BeansObservables.observeValue(model, "schuhName"); //$NON-NLS-1$
        ctx.bindValue(textNameObservable, modelNameObservable, null, null);

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
                    setErrorMessage(Messages.SchuhDialog_Fehlermeldung);
                    return ValidationStatus.error("Mist"); //$NON-NLS-1$
                }
            }
        };

        ctx.addValidationStatusProvider(multi);

        ctx.bindValue(multi.observeValidatedValue(textNameObservable), modelNameObservable);
    }

    @Override
    protected Control createButtonBar(final Composite p) {
        final Control c = super.createButtonBar(p);
        final Button ok = getButton(OK);
        ok.setEnabled(false);
        return c;
    }

    @Override
    protected void buttonPressed(final int buttonId) {
        if (IDialogConstants.OK_ID == buttonId) {
            // speichern
            LOG.debug("Neue Schuhe speichern"); //$NON-NLS-1$

            boolean confirm = true;

            final boolean exists = databaseAccess.existsSchuh(model.getAthlete(), model.getSchuhName());

            if (exists) {
                confirm = MessageDialog.openConfirm(parent, "Es ist bereits ein Schuh mit diesem Namen erfasst!", Messages.HealthDialog_1);
            }
            if (confirm) {
                Display.getDefault().asyncExec(new Runnable() {

                    private List<IShoe> all;

                    @Override
                    public void run() {
                        LOG.info("write in database"); //$NON-NLS-1$
                        final IShoe schuh = CommonTransferFactory.createSchuh(model.getAthlete(), model.getSchuhName(), "imageicon"); //$NON-NLS-1$
                        databaseAccess.saveOrUpdate(schuh);

                        all = SchuhCache.getInstance().getAll();
                        all.add(schuh);
                        SchuhCache.getInstance().addAll(all);
                    }
                });
            }

        }
        super.buttonPressed(buttonId);
    }
}
