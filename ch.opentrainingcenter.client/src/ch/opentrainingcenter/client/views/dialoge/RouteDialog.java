package ch.opentrainingcenter.client.views.dialoge;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.MultiValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.cache.RouteCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("deprecation")
public class RouteDialog extends TitleAreaDialog {

    private static final Logger LOG = Logger.getLogger(RouteDialog.class);

    private final StreckeModel model;

    private DataBindingContext ctx;
    private Text name;
    private Text beschreibung;

    private final IDatabaseAccess databaseAccess;

    private final ITraining training;

    public RouteDialog(final Shell parent, final IDatabaseAccess databaseAccess, final ITraining training) {
        super(parent);
        this.databaseAccess = databaseAccess;
        this.training = training;
        model = new StreckeModel(training.getAthlete());
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setTitle(Messages.RouteDialog_0);
        setMessage(NLS.bind(Messages.RouteDialog_1, TimeHelper.convertDateToString(training.getDatum())));
        setTitleImage(Activator.getImageDescriptor(IImageKeys.ROUTE_WIZ).createImage());

        final Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(separator);

        final Composite container = new Composite(parent, SWT.NONE);

        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(container);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container);

        // ---
        final Label labelName = new Label(container, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, true).applyTo(labelName);
        labelName.setText(Messages.RouteDialog_2);

        name = new Text(container, SWT.BORDER);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).applyTo(name);

        // ---------------------
        final Label labelBeschreibung = new Label(container, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, true).applyTo(labelBeschreibung);
        labelBeschreibung.setText(Messages.RouteDialog_3);

        beschreibung = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        final GridData noteGd = new GridData();
        noteGd.grabExcessHorizontalSpace = true;
        noteGd.grabExcessVerticalSpace = true;
        noteGd.minimumHeight = 80;
        noteGd.horizontalAlignment = SWT.FILL;
        noteGd.verticalAlignment = SWT.CENTER;
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
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    final ITraining tr = ((ConcreteImported) training).getTraining();
                    final IRoute newRoute = CommonTransferFactory.createRoute(model.getName(), model.getBeschreibung(), tr);
                    databaseAccess.saveOrUpdate(newRoute);

                    tr.setRoute(newRoute);
                    databaseAccess.saveOrUpdate(tr);
                    RouteCache.getInstance().addAll(Arrays.asList(newRoute));
                }
            });
            super.buttonPressed(buttonId);
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
                if (RouteCache.getInstance().contains(nameVonModel)) {
                    getButton(OK).setEnabled(false);
                    setErrorMessage(Messages.RouteDialog_Route_existiert_bereits);
                    return ValidationStatus.error("Mist"); //$NON-NLS-1$
                }
                if (nameValid) {
                    getButton(OK).setEnabled(true);
                    setErrorMessage(null);
                    return ValidationStatus.ok();
                } else {
                    getButton(OK).setEnabled(false);
                    setErrorMessage(Messages.RouteDialog_5);
                    return ValidationStatus.error("Mist"); //$NON-NLS-1$
                }
            }
        };

        ctx.addValidationStatusProvider(multi);

        ctx.bindValue(multi.observeValidatedValue(textNameObservable), modelNameObservable);
    }
}
