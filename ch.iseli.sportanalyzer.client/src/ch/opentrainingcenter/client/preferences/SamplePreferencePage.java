package ch.opentrainingcenter.client.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IAthlete;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built
 * into JFace that allows us to create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main plug-in class. That way, preferences can be accessed directly via
 * the preference store.
 */

public class SamplePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private final List<IAthlete> allAthletes;

    public SamplePreferencePage() {
        super(GRID);
        setDescription(Messages.SamplePreferencePage_0);
        allAthletes = DatabaseAccessFactory.getDatabaseAccess().getAllAthletes();
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various types of preferences. Each field editor knows how to save and
     * restore itself.
     */
    @Override
    public void createFieldEditors() {
        final Composite fieldEditorParent = getFieldEditorParent();

        addField(new DirectoryFieldEditor(PreferenceConstants.GPS_FILE_LOCATION, Messages.SamplePreferencePage_1, fieldEditorParent));

        addField(new DirectoryFieldEditor(PreferenceConstants.GPS_FILE_LOCATION_PROG, Messages.SamplePreferencePage_2, fieldEditorParent));

        final List<String[]> vals = new ArrayList<String[]>();
        for (final IAthlete ath : allAthletes) {
            vals.add(new String[] { ath.getName(), String.valueOf(ath.getId()) });
        }
        final ComboFieldEditor comboField = new ComboFieldEditor(PreferenceConstants.ATHLETE_ID, Messages.SamplePreferencePage_3, vals.toArray(new String[0][0]), fieldEditorParent);
        addField(comboField);
        comboField.setEnabled(false, fieldEditorParent);

        final IntegerFieldEditor sb = new IntegerFieldEditor(PreferenceConstants.SB, Messages.SamplePreferencePage_4, fieldEditorParent);
        sb.setLabelText(Messages.SamplePreferencePage_5);
        sb.setValidRange(91, 95);
        sb.setErrorMessage(Messages.SamplePreferencePage_6);
        sb.setTextLimit(2);
        addField(sb);

        final IntegerFieldEditor extIntervall = new IntegerFieldEditor(PreferenceConstants.EXTINTERVALL, Messages.SamplePreferencePage_7, fieldEditorParent);
        extIntervall.setLabelText(Messages.SamplePreferencePage_8);
        extIntervall.setValidRange(85, 89);
        extIntervall.setErrorMessage(Messages.SamplePreferencePage_9);
        extIntervall.setTextLimit(2);
        addField(extIntervall);

        final IntegerFieldEditor intDl = new IntegerFieldEditor(PreferenceConstants.INTDL, Messages.SamplePreferencePage_10, fieldEditorParent);
        intDl.setLabelText(Messages.SamplePreferencePage_11);
        intDl.setValidRange(75, 80);
        intDl.setErrorMessage(Messages.SamplePreferencePage_12);
        intDl.setTextLimit(2);
        addField(intDl);

        final IntegerFieldEditor extDl = new IntegerFieldEditor(PreferenceConstants.EXTDL, Messages.SamplePreferencePage_13, fieldEditorParent);
        extDl.setLabelText(Messages.SamplePreferencePage_14);
        extDl.setValidRange(70, 75);
        extDl.setErrorMessage(Messages.SamplePreferencePage_15);
        extDl.setTextLimit(2);
        addField(extDl);

        final IntegerFieldEditor anaerobe = new IntegerFieldEditor(PreferenceConstants.ANAEROBE, Messages.SamplePreferencePage_16, fieldEditorParent);
        anaerobe.setValidRange(90, 100);
        anaerobe.setErrorMessage(Messages.SamplePreferencePage_17);
        anaerobe.setTextLimit(2);
        addField(anaerobe);

        addField(new ColorFieldEditor(PreferenceConstants.ANAEROBE_COLOR, Messages.SamplePreferencePage_18, getFieldEditorParent()));

        final IntegerFieldEditor schwellenzone = new IntegerFieldEditor(PreferenceConstants.SCHWELLENZONE, Messages.SamplePreferencePage_19, fieldEditorParent);
        schwellenzone.setValidRange(80, 90);
        schwellenzone.setErrorMessage(Messages.SamplePreferencePage_20);
        schwellenzone.setTextLimit(2);
        addField(schwellenzone);

        addField(new ColorFieldEditor(PreferenceConstants.SCHWELLENZONE_COLOR, Messages.SamplePreferencePage_21, getFieldEditorParent()));

        final IntegerFieldEditor aerobe = new IntegerFieldEditor(PreferenceConstants.AEROBE, Messages.SamplePreferencePage_22, fieldEditorParent);
        aerobe.setErrorMessage(Messages.SamplePreferencePage_23);
        aerobe.setValidRange(50, 80);
        aerobe.setTextLimit(2);
        addField(aerobe);

        addField(new ColorFieldEditor(PreferenceConstants.AEROBE_COLOR, Messages.SamplePreferencePage_24, getFieldEditorParent()));

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

}