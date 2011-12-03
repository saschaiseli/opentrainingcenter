package ch.iseli.sportanalyzer.client.preferences;

import java.util.List;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
        fieldEditorParent.setSize(50, 60);
        addField(new DirectoryFieldEditor(PreferenceConstants.GPS_FILE_LOCATION, "Ort der GPS Daten:", fieldEditorParent));

        final String[][] entryNamesAndValues = new String[][] { {} };
        int i = 0;
        for (final IAthlete ath : allAthletes) {
            entryNamesAndValues[i] = new String[] { ath.getName(), String.valueOf(ath.getId()) };
            i++;
        }
        final ComboFieldEditor comboField = new ComboFieldEditor(PreferenceConstants.ATHLETE_NAME, "Sportler:", entryNamesAndValues, fieldEditorParent);
        addField(comboField);
        comboField.setPropertyChangeListener(new IPropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent event) {
                System.out.println(event.getNewValue());
            }
        });

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