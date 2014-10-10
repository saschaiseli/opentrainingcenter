package ch.opentrainingcenter.client.views.dialoge;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.HealthCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

/**
 * Dialog wo tägliche Daten, wie Gewicht und Ruhepuls erfasst werden können.
 * 
 * @author sascha
 * 
 */
public class HealthDialog extends TitleAreaDialog {

    private static final Logger LOG = Logger.getLogger(HealthDialog.class);

    private Text pulsText;
    private Text gewichtText;

    private Label errorLabel;

    private DateTime dateTime;

    private final Shell parent;
    private final IDatabaseAccess db;
    private final IAthlete athlete;

    private final Date date;

    private final IHealth healt;

    public HealthDialog(final Shell parent) {
        this(parent, null, Activator.getDefault().getPreferenceStore(), new Date());
    }

    public HealthDialog(final Shell parent, final IHealth health) {
        this(parent, null, Activator.getDefault().getPreferenceStore(), health.getDateofmeasure());
    }

    public HealthDialog(final Shell parent, final IDatabaseAccess databaseAccess, final IPreferenceStore store, final Date date) {
        super(parent);
        this.parent = parent;
        if (databaseAccess == null) {
            final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
            this.db = service.getDatabaseAccess();
        } else {
            this.db = databaseAccess;
        }
        this.date = date;
        final String id = store.getString(PreferenceConstants.ATHLETE_ID);
        athlete = db.getAthlete(Integer.valueOf(id));
        healt = db.getHealth(athlete, date);
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
        final GridData gdPulsText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdPulsText.widthHint = 80;
        pulsText.setLayoutData(gdPulsText);

        pulsText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                validate();
            }

        });

        final Label gewichtLabel = new Label(containerTextFields, SWT.NONE);
        gewichtLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        gewichtLabel.setText(Messages.HealthDialog3);

        gewichtText = new Text(containerTextFields, SWT.BORDER);
        final GridData gdGewichtText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdGewichtText.widthHint = 80;
        gewichtText.setLayoutData(gdGewichtText);

        gewichtText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                validate();
            }
        });
        errorLabel = new Label(c, SWT.NONE);
        errorLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        errorLabel.setVisible(true);
        new Label(c, SWT.NONE);
        new Label(c, SWT.NONE);

        if (healt != null) {
            gewichtText.setText(healt.getWeight().toString());
            pulsText.setText(healt.getCardio().toString());
            cal.setTime(healt.getDateofmeasure());
            dateTime.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }
        return c;
    }

    private void validate() {
        LOG.debug("validate"); //$NON-NLS-1$
        final boolean pulsValid = pulsValid(pulsText.getText());
        final boolean gewichtValid = gewichtValid(gewichtText.getText());

        LOG.debug("gewichtValid " + gewichtValid); //$NON-NLS-1$
        final Button okButton = getButton(OK);
        if (okButton == null || okButton.isDisposed()) {
            return;
        }
        if (pulsValid && gewichtValid) {
            okButton.setEnabled(true);
            setErrorMessage(null);
        } else {
            okButton.setEnabled(false);
            setErrorMessage(Messages.HealthDialog_3);
        }
    }

    private boolean gewichtValid(final Object gewichtObj) {
        final boolean gewichtValid;
        try {
            final Double gewicht = Double.valueOf(gewichtObj.toString());
            LOG.debug("gewicht " + gewicht); //$NON-NLS-1$
            gewichtValid = gewicht != null && gewicht.doubleValue() > 40;
        } catch (final NumberFormatException nfe) {
            return false;
        }
        return gewichtValid;
    }

    private boolean pulsValid(final Object pulsObject) {
        final boolean pulsValid;
        try {
            final Integer puls = Integer.valueOf(pulsObject.toString());
            pulsValid = puls != null && puls > 0;
        } catch (final NumberFormatException nfe) {
            return false;
        }
        return pulsValid;
    }

    @Override
    protected void buttonPressed(final int buttonId) {
        if (IDialogConstants.OK_ID == buttonId) {
            final Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.set(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
            final String puls = pulsText.getText();
            final String gewicht = gewichtText.getText();
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {

                    final Date date = cal.getTime();
                    IHealth health = db.getHealth(athlete, date);
                    boolean confirm = true;
                    if (health != null) {
                        confirm = MessageDialog.openConfirm(parent, Messages.HealthDialog_0, Messages.HealthDialog_1);
                    }
                    if (confirm) {
                        final Integer ruhePuls = Integer.valueOf(puls);
                        final Double weight = Double.valueOf(gewicht);
                        if (health != null) {
                            health.setCardio(ruhePuls);
                            health.setWeight(weight);
                            health.setDateofmeasure(cal.getTime());
                        } else {
                            health = CommonTransferFactory.createHealth(athlete, weight, ruhePuls, cal.getTime());
                        }

                        db.saveOrUpdate(health);
                        HealthCache.getInstance().addAll(Arrays.asList(health));
                    }
                }
            });
        }
        super.buttonPressed(buttonId);
    }

    @Override
    protected Control createButtonBar(final Composite p) {
        final Control c = super.createButtonBar(p);
        validate();
        return c;
    }
}
