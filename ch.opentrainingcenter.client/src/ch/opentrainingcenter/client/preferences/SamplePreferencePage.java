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
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.helper.GpsFileNameFilter;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.importer.ExtensionHelper;
import ch.opentrainingcenter.importer.ImporterFactory;
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
        setDescription(Messages.SamplePreferencePage0);
        allAthletes = DatabaseAccessFactory.getDatabaseAccess().getAllAthletes();
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    @Override
    public void createFieldEditors() {
        final Composite fieldEditorParent = getFieldEditorParent();

        addField(new DirectoryFieldEditor(PreferenceConstants.GPS_FILE_LOCATION, Messages.SamplePreferencePage1, fieldEditorParent));

        addField(new DirectoryFieldEditor(PreferenceConstants.GPS_FILE_LOCATION_PROG, Messages.SamplePreferencePage2, fieldEditorParent));

        final List<String[]> vals = new ArrayList<String[]>();
        for (final IAthlete ath : allAthletes) {
            vals.add(new String[] { ath.getName(), String.valueOf(ath.getId()) });
        }
        final ComboFieldEditor comboField = new ComboFieldEditor(PreferenceConstants.ATHLETE_ID, Messages.SamplePreferencePage3, vals
                .toArray(new String[0][0]), fieldEditorParent);
        addField(comboField);
        comboField.setEnabled(false, fieldEditorParent);

        final IntegerFieldEditor sb = new IntegerFieldEditor(PreferenceConstants.SB, Messages.SamplePreferencePage4, fieldEditorParent);
        sb.setLabelText(Messages.SamplePreferencePage5);
        sb.setValidRange(91, 95);
        sb.setErrorMessage(Messages.SamplePreferencePage6);
        sb.setTextLimit(2);
        addField(sb);

        final IntegerFieldEditor extIntervall = new IntegerFieldEditor(PreferenceConstants.EXTINTERVALL, Messages.SamplePreferencePage7,
                fieldEditorParent);
        extIntervall.setLabelText(Messages.SamplePreferencePage8);
        extIntervall.setValidRange(85, 89);
        extIntervall.setErrorMessage(Messages.SamplePreferencePage9);
        extIntervall.setTextLimit(2);
        addField(extIntervall);

        final IntegerFieldEditor intDl = new IntegerFieldEditor(PreferenceConstants.INTDL, Messages.SamplePreferencePage10,
                fieldEditorParent);
        intDl.setLabelText(Messages.SamplePreferencePage11);
        intDl.setValidRange(75, 80);
        intDl.setErrorMessage(Messages.SamplePreferencePage12);
        intDl.setTextLimit(2);
        addField(intDl);

        final IntegerFieldEditor extDl = new IntegerFieldEditor(PreferenceConstants.EXTDL, Messages.SamplePreferencePage13,
                fieldEditorParent);
        extDl.setLabelText(Messages.SamplePreferencePage14);
        extDl.setValidRange(70, 75);
        extDl.setErrorMessage(Messages.SamplePreferencePage15);
        extDl.setTextLimit(2);
        addField(extDl);

        final IntegerFieldEditor recom = new IntegerFieldEditor(PreferenceConstants.RECOM, Messages.SamplePreferencePage_0, fieldEditorParent);
        addField(recom);
        addField(new ColorFieldEditor(PreferenceConstants.RECOM_COLOR, "", fieldEditorParent)); //$NON-NLS-1$

        final IntegerFieldEditor ga1 = new IntegerFieldEditor(PreferenceConstants.GA1, "", fieldEditorParent); //$NON-NLS-1$
        addField(ga1);
        addField(new ColorFieldEditor(PreferenceConstants.GA1_COLOR, "", fieldEditorParent)); //$NON-NLS-1$

        final IntegerFieldEditor ga12 = new IntegerFieldEditor(PreferenceConstants.GA12, "", fieldEditorParent); //$NON-NLS-1$
        addField(ga12);
        addField(new ColorFieldEditor(PreferenceConstants.GA12_COLOR, "", fieldEditorParent)); //$NON-NLS-1$

        final IntegerFieldEditor ga2 = new IntegerFieldEditor(PreferenceConstants.GA2, "", fieldEditorParent); //$NON-NLS-1$
        addField(ga2);
        addField(new ColorFieldEditor(PreferenceConstants.GA2_COLOR, "", fieldEditorParent)); //$NON-NLS-1$

        final IntegerFieldEditor wsa = new IntegerFieldEditor(PreferenceConstants.WSA, "", fieldEditorParent); //$NON-NLS-1$
        wsa.setEnabled(false, fieldEditorParent);
        addField(wsa);
        addField(new ColorFieldEditor(PreferenceConstants.WSA_COLOR, "", fieldEditorParent)); //$NON-NLS-1$
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