package ch.opentrainingcenter.client.views.dialoge;

import java.io.File;
import java.util.Date;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.cache.SchuhCache;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.ImageScaler;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.schuh.SchuhModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class SchuhDialog extends TitleAreaDialog {

    private static final Logger LOG = Logger.getLogger(SchuhDialog.class);
    private final IDatabaseAccess databaseAccess;
    private Text schuhName;
    private DateTime kaufDatum;
    private Text preis;

    private DataBindingContext ctx;

    private final SchuhModel model;

    private final Shell parent;

    private Text bild;
    private final String location;
    private final String title;

    public SchuhDialog(final Shell parent, final IDatabaseAccess databaseAccess, final IAthlete athlete, final String title) {
        super(parent);
        this.parent = parent;
        this.databaseAccess = databaseAccess;
        this.title = title;
        model = new SchuhModel(athlete);
        model.setKaufdatum(new Date());
        // model.setId(-1); // neuer eintrag
        location = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);
    }

    public SchuhDialog(final Shell parent, final IDatabaseAccess databaseAccess, final IShoe shoe, final String title) {
        super(parent);
        this.parent = parent;
        this.databaseAccess = databaseAccess;
        this.title = title;
        model = new SchuhModel(shoe.getAthlete());
        model.setKaufdatum(shoe.getKaufdatum());
        model.setId(shoe.getId());
        model.setImage(shoe.getImageicon());
        model.setPreis(shoe.getPreis());
        model.setSchuhName(shoe.getSchuhname());

        location = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setTitle(title);
        setMessage(Messages.SchuhDialog_Beschreibung);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.SHOE_64).createImage());

        final Composite container = new Composite(parent, SWT.NONE);

        GridLayoutFactory.fillDefaults().numColumns(3).margins(20, 0).applyTo(container);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container);

        // --- Name -------------------------------------------------------
        final Label labelName = new Label(container, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, true).applyTo(labelName);
        labelName.setText(Messages.SchuhDialog_Label_Name_Marke);

        schuhName = new Text(container, SWT.BORDER);
        GridDataFactory.fillDefaults().span(2, 1).align(SWT.FILL, SWT.CENTER).grab(true, true).applyTo(schuhName);
        // --- Datum -------------------------------------------------------
        final Label labelDatum = new Label(container, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, true).applyTo(labelDatum);
        labelDatum.setText(Messages.SchuhDialog_KaufdatumLabel);

        kaufDatum = new DateTime(container, SWT.CALENDAR | SWT.BORDER);
        kaufDatum.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final int year = kaufDatum.getYear();
                final int month = kaufDatum.getMonth();
                final int day = kaufDatum.getDay();
                final org.joda.time.DateTime dateTime = new org.joda.time.DateTime(year, month, day, 0, 0);
                model.setKaufdatum(dateTime.toDate());
                super.widgetSelected(e);
            }
        });

        if (model.getKaufdatum() != null) {
            final org.joda.time.DateTime dt = new org.joda.time.DateTime(model.getKaufdatum().getTime());
            kaufDatum.setDate(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
        }

        GridDataFactory.fillDefaults().span(2, 1).align(SWT.FILL, SWT.CENTER).grab(true, true).applyTo(kaufDatum);
        // --- Preis -------------------------------------------------------
        final Label labelPreis = new Label(container, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, true).applyTo(labelPreis);
        labelPreis.setText(Messages.SchuhDialog_PreisLabel);

        preis = new Text(container, SWT.BORDER);
        GridDataFactory.fillDefaults().span(2, 1).align(SWT.FILL, SWT.CENTER).grab(true, true).applyTo(preis);
        // --- Image -------------------------------------------------------
        final Label labelImage = new Label(container, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).grab(false, true).applyTo(labelImage);
        labelImage.setText(Messages.SchuhDialog_BildLabel);

        bild = new Text(container, SWT.BORDER);
        GridDataFactory.fillDefaults().span(1, 1).align(SWT.FILL, SWT.CENTER).grab(true, true).applyTo(bild);

        final Button add = new Button(container, SWT.PUSH);
        add.setText(Messages.SchuhDialog_add);
        GridDataFactory.fillDefaults().span(1, 1).align(SWT.RIGHT, SWT.CENTER).grab(false, true).applyTo(add);

        add.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final FileDialog dialog = new FileDialog(parent.getShell());
                final String pathname = dialog.open();
                model.setImage(pathname);
            }
        });

        // -- Databinding
        ctx = new DataBindingContext();
        initDataBindings();

        return container;
    }

    private void initDataBindings() {
        // -- Name -------------------
        final IObservableValue textNameObservable = SWTObservables.observeText(schuhName, SWT.Modify);
        final IObservableValue modelNameObservable = BeansObservables.observeValue(model, "schuhName"); //$NON-NLS-1$
        ctx.bindValue(textNameObservable, modelNameObservable, null, null);
        // Preis
        final IObservableValue textPreisObservable = SWTObservables.observeText(preis, SWT.Modify);
        final IObservableValue modelPreisObservable = BeansObservables.observeValue(model, "preis"); //$NON-NLS-1$
        ctx.bindValue(textPreisObservable, modelPreisObservable, null, null);
        // -- Bild -------------------
        final IObservableValue textBildObservable = SWTObservables.observeText(bild, SWT.Modify);
        final IObservableValue modelBildObservable = BeansObservables.observeValue(model, "image"); //$NON-NLS-1$
        ctx.bindValue(textBildObservable, modelBildObservable, null, null);

        final MultiValidator multi = new MultiValidator() {
            @Override
            protected IStatus validate() {
                boolean nameValid = false;
                final Object nameVal = textNameObservable.getValue();

                final String nameVonModel = nameVal.toString();
                if (nameVonModel != null && nameVonModel.length() > 3) {
                    nameValid = true;
                }
                final String preisVal = textPreisObservable.getValue().toString();
                Integer pr = null;
                try {
                    pr = Integer.parseInt(preisVal);
                } catch (final NumberFormatException nfe) {
                    return ValidationStatus.error("Mist"); //$NON-NLS-1$
                }
                if (nameValid && pr != null && pr.intValue() > 0) {
                    getButton(OK).setEnabled(true);
                    setErrorMessage(null);
                    return ValidationStatus.ok();
                } else if (pr != null && pr.intValue() == 0) {
                    getButton(OK).setEnabled(false);
                    setErrorMessage(Messages.SchuhDialog_Error_Preis);
                    return ValidationStatus.error("Mist"); //$NON-NLS-1$
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
        System.out.println(Messages.SchuhDialog_ModelLabel + model.toString());
        if (IDialogConstants.OK_ID == buttonId) {
            // speichern
            LOG.debug("Neue Schuhe speichern"); //$NON-NLS-1$

            boolean confirm = true;

            if (model.getId() > 0) {
                confirm = MessageDialog.openConfirm(parent, Messages.SchuhDialog_Error_Schuhname, Messages.HealthDialog_1);
            }
            if (confirm) {
                Display.getDefault().asyncExec(new Runnable() {

                    private List<IShoe> all;

                    @Override
                    public void run() {

                        LOG.info("write in database"); //$NON-NLS-1$

                        final IShoe schuh;
                        final String imageIcon = model.getImage();
                        final Date kaufDatum = model.getKaufdatum();
                        final int preis = model.getPreis();
                        final String name = model.getSchuhName();
                        if (model.getId() > 0) {
                            schuh = SchuhCache.getInstance().get(String.valueOf(model.getId()));
                            schuh.setImageicon(imageIcon);
                            schuh.setKaufdatum(kaufDatum);
                            schuh.setPreis(preis);
                            schuh.setSchuhname(name);
                        } else {
                            schuh = CommonTransferFactory.createSchuh(model.getAthlete(), name, imageIcon, preis, kaufDatum);
                        }
                        databaseAccess.saveOrUpdate(schuh);

                        LOG.info("Konvert Image und Copy it"); //$NON-NLS-1$
                        if (model.getImage() != null) {
                            final Image image = new Image(Display.getDefault(), model.getImage());
                            final Image scaled = ImageScaler.scale(image, 200);
                            image.dispose();

                            final ImageLoader loader = new ImageLoader();
                            loader.data = new ImageData[] { scaled.getImageData() };
                            final String newFileName = location + File.separator + "Schuh_" + schuh.getId() + ".png"; //$NON-NLS-1$ //$NON-NLS-2$
                            loader.save(newFileName, SWT.IMAGE_PNG);

                            final File destination = new File(newFileName);
                            model.setImage(destination.getAbsolutePath());
                            schuh.setImageicon(destination.getAbsolutePath());
                            databaseAccess.saveOrUpdate(schuh);
                        }
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
