package ch.opentrainingcenter.client.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.Sport;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class AllgemeinePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private final List<IAthlete> allAthletes;

    private final IDatabaseAccess databaseAccess;

    private BooleanFieldEditor running;

    private BooleanFieldEditor biking;

    public AllgemeinePreferencePage() {
        super(GRID);
        setDescription(Messages.SamplePreferencePage_1);
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        if (DBSTATE.OK.equals(ApplicationContext.getApplicationContext().getDbState().getState())) {
            allAthletes = databaseAccess.getAllAthletes();
        } else {
            allAthletes = Collections.emptyList();
        }
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    @Override
    public void createFieldEditors() {
        final Composite training = getFieldEditorParent();

        final Label head = new Label(training, SWT.NONE);
        head.setText(Messages.AllgemeinePreferencePage_Sportarten);

        running = new BooleanFieldEditor(Sport.RUNNING.getMessage(), Sport.RUNNING.getTranslated(), getFieldEditorParent());
        biking = new BooleanFieldEditor(Sport.BIKING.getMessage(), Sport.BIKING.getTranslated(), getFieldEditorParent());
        addField(running);
        addField(biking);

        final List<String[]> vals = new ArrayList<String[]>();
        for (final IAthlete ath : allAthletes) {
            vals.add(new String[] { ath.getName(), String.valueOf(ath.getId()) });
        }
        final ComboFieldEditor comboField = new ComboFieldEditor(PreferenceConstants.ATHLETE_ID, Messages.SamplePreferencePage3,
                vals.toArray(new String[0][0]), training);
        addField(comboField);
        comboField.setEnabled(false, training);

    }

    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void checkState() {
        final boolean valid = running.getBooleanValue() || biking.getBooleanValue();
        System.out.println(valid);
        setValid(valid);
        if (!valid) {
            setErrorMessage(Messages.AllgemeinePreferencePage_ErrorMessage_EineSportartwaehlen);
        } else {
            setErrorMessage(null);
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        super.propertyChange(event);
        final Object source = event.getSource();
        // if (source instanceof DirectoryFieldEditor) {
        // final DirectoryFieldEditor dfe = (DirectoryFieldEditor) source;
        // final String preferenceName = dfe.getPreferenceName();
        // if
        // (preferenceName.equals(PreferenceConstants.GPS_FILE_LOCATION_PROG)) {
        //                LOGGER.debug("Neuer Ort für GPS files ausgewählt. Files von: " + event.getOldValue() + " nach: " + event.getNewValue() + " kopieren"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        //
        // try {
        // ImporterFactory.createFileCopy().copyFiles(event.getOldValue().toString(),
        // event.getNewValue().toString(),
        // new GpsFileNameFilter(ExtensionHelper.getConverters()));
        // } catch (final IOException e) {
        // LOGGER.error(e.getMessage(), e.fillInStackTrace());
        // }
        // }
        // }
        if (source instanceof BooleanFieldEditor) {
            checkState();
        }
    }

}