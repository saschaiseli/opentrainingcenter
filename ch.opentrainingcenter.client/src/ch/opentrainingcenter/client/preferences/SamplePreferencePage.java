package ch.opentrainingcenter.client.preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.helper.GpsFileNameFilter;
import ch.opentrainingcenter.core.importer.ExtensionHelper;
import ch.opentrainingcenter.core.importer.ImporterFactory;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;

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

public class SamplePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final Logger LOGGER = Logger.getLogger(SamplePreferencePage.class);

    private final List<IAthlete> allAthletes;

    public SamplePreferencePage() {
        super(GRID);
        setDescription(Messages.SamplePreferencePage_1);
        allAthletes = DatabaseAccessFactory.getDatabaseAccess().getAllAthletes();
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    @Override
    public void createFieldEditors() {
        final Composite training = getFieldEditorParent();

        final List<String[]> vals = new ArrayList<String[]>();
        for (final IAthlete ath : allAthletes) {
            vals.add(new String[] { ath.getName(), String.valueOf(ath.getId()) });
        }
        final ComboFieldEditor comboField = new ComboFieldEditor(PreferenceConstants.ATHLETE_ID, Messages.SamplePreferencePage3,
                vals.toArray(new String[0][0]), training);
        addField(comboField);
        comboField.setEnabled(false, training);

        final IntegerFieldEditor recom = new IntegerFieldEditor(PreferenceConstants.RECOM, Messages.SamplePreferencePage_2, training);
        addField(recom);
        addField(new ColorFieldEditor(PreferenceConstants.RECOM_COLOR, "", training)); //$NON-NLS-1$

        final IntegerFieldEditor ga1 = new IntegerFieldEditor(PreferenceConstants.GA1, Messages.SamplePreferencePage_4, training);
        addField(ga1);
        addField(new ColorFieldEditor(PreferenceConstants.GA1_COLOR, "", training)); //$NON-NLS-1$

        final IntegerFieldEditor ga12 = new IntegerFieldEditor(PreferenceConstants.GA12, Messages.SamplePreferencePage_5, training);
        addField(ga12);
        addField(new ColorFieldEditor(PreferenceConstants.GA12_COLOR, "", training)); //$NON-NLS-1$

        final IntegerFieldEditor ga2 = new IntegerFieldEditor(PreferenceConstants.GA2, Messages.SamplePreferencePage_6, training);
        addField(ga2);
        addField(new ColorFieldEditor(PreferenceConstants.GA2_COLOR, "", training)); //$NON-NLS-1$

        final IntegerFieldEditor wsa = new IntegerFieldEditor(PreferenceConstants.WSA, Messages.SamplePreferencePage_7, training);
        wsa.setEnabled(false, training);
        addField(wsa);
        addField(new ColorFieldEditor(PreferenceConstants.WSA_COLOR, "", training)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        super.propertyChange(event);
        final Object source = event.getSource();
        if (source instanceof DirectoryFieldEditor) {
            final DirectoryFieldEditor dfe = (DirectoryFieldEditor) source;
            final String preferenceName = dfe.getPreferenceName();
            if (preferenceName.equals(PreferenceConstants.GPS_FILE_LOCATION_PROG)) {
                LOGGER.debug("Neuer Ort für GPS files ausgewählt. Files von: " + event.getOldValue() + " nach: " + event.getNewValue() + " kopieren"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

                try {
                    ImporterFactory.createFileCopy().copyFiles(event.getOldValue().toString(), event.getNewValue().toString(),
                            new GpsFileNameFilter(ExtensionHelper.getConverters()));
                } catch (final IOException e) {
                    LOGGER.error(e.getMessage(), e.fillInStackTrace());
                }
            }
        }
    }

}