package ch.iseli.sportanalyzer.client.preferences;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.Application;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.helper.DaoHelper;
import ch.iseli.sportanalyzer.db.IImportedDao;
import ch.opentrainingcenter.transfer.impl.Athlete;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built
 * into JFace that allows us to create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main plug-in class. That way, preferences can be accessed directly via
 * the preference store.
 */

public class SamplePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private final List<Athlete> allAthletes;

    public SamplePreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Einige Einstellungen");
        IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor(Application.CH_OPENTRAININGDATABASE_DB);
        final IImportedDao dao = (IImportedDao) DaoHelper.getDao(daos, IImportedDao.EXTENSION_POINT_NAME);
        allAthletes = dao.getAllAthletes();
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various types of preferences. Each field editor knows how to save and
     * restore itself.
     */
    @Override
    public void createFieldEditors() {
        Composite fieldEditorParent = getFieldEditorParent();
        fieldEditorParent.setSize(50, 60);
        addField(new DirectoryFieldEditor(PreferenceConstants.GPS_FILE_LOCATION, "Ort der GPS Daten:", fieldEditorParent));

        String[][] entryNamesAndValues = new String[][] { {} };
        int i = 0;
        for (Athlete ath : allAthletes) {
            entryNamesAndValues[i] = new String[] { ath.getName(), String.valueOf(ath.getId()) };
            i++;
        }
        ComboFieldEditor comboField = new ComboFieldEditor(PreferenceConstants.ATHLETE_NAME, "Sportler:", entryNamesAndValues, fieldEditorParent);
        addField(comboField);
        comboField.setPropertyChangeListener(new IPropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent event) {
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
    public void init(IWorkbench workbench) {
    }

}