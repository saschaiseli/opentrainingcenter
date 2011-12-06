package ch.iseli.sportanalyzer.client.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
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
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Einige Einstellungen");
        allAthletes = DatabaseAccessFactory.getDatabaseAccess().getAllAthletes();
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various types of preferences. Each field editor knows how to save and
     * restore itself.
     */
    @Override
    public void createFieldEditors() {
        final Composite fieldEditorParent = getFieldEditorParent();
        fieldEditorParent.setSize(50, 20);
        addField(new DirectoryFieldEditor(PreferenceConstants.GPS_FILE_LOCATION, "Ort der GPS Daten:", fieldEditorParent));

        final List<String[]> vals = new ArrayList<String[]>();
        for (final IAthlete ath : allAthletes) {
            vals.add(new String[] { ath.getName(), String.valueOf(ath.getId()) });
        }
        final ComboFieldEditor comboField = new ComboFieldEditor(PreferenceConstants.ATHLETE_ID, "Sportler:", vals.toArray(new String[0][0]), fieldEditorParent);
        addField(comboField);
        comboField.setEnabled(false, fieldEditorParent);

        final IntegerFieldEditor sb = new IntegerFieldEditor(PreferenceConstants.SB, "Spitzenbereich SB", fieldEditorParent);
        sb.setLabelText("Spitzenbereich [ca. 91-95% HFmax, Eingabe in %]");
        sb.setValidRange(91, 95);
        sb.setErrorMessage("Spitzenbereich liegt zwischen 91% und 95% der maximalen Herzfrequenz");
        sb.setTextLimit(2);
        addField(sb);

        final IntegerFieldEditor extIntervall = new IntegerFieldEditor(PreferenceConstants.EXTINTERVALL, "Spitzenbereich SB", fieldEditorParent);
        extIntervall.setLabelText("Extensives Intervall [ca. 85%-89% HFmax, Eingabe in %]");
        extIntervall.setValidRange(85, 89);
        extIntervall.setErrorMessage("Extensives Intervall liegt zwischen 85% und 89% der maximalen Herzfrequenz");
        extIntervall.setTextLimit(2);
        addField(extIntervall);

        final IntegerFieldEditor intDl = new IntegerFieldEditor(PreferenceConstants.INTDL, "Spitzenbereich SB", fieldEditorParent);
        intDl.setLabelText("Intensiver Dauerlauf [ca. 75%-80% HFmax, Eingabe in %]");
        intDl.setValidRange(75, 80);
        intDl.setErrorMessage("Intensiver Dauerlauf liegt zwischen 85% und 89% der maximalen Herzfrequenz");
        intDl.setTextLimit(2);
        addField(intDl);

        final IntegerFieldEditor extDl = new IntegerFieldEditor(PreferenceConstants.EXTDL, "Spitzenbereich SB", fieldEditorParent);
        extDl.setLabelText("Extensiver Dauerlauf [ca. 70%-75% HFmax, Eingabe in %]");
        extDl.setValidRange(70, 75);
        extDl.setErrorMessage("Extensiver Dauerlauf liegt zwischen 70% und 75% der maximalen Herzfrequenz");
        extDl.setTextLimit(2);
        addField(extDl);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(final IWorkbench workbench) {
    }

}