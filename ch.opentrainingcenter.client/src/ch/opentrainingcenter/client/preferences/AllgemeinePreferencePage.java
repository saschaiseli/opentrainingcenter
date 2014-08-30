package ch.opentrainingcenter.client.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
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
    private BooleanFieldEditor other;

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

        final Label sport = new Label(training, SWT.NONE);
        sport.setText(Messages.ChartPreferencePage_6);

        final Combo comboSport = new Combo(training, SWT.READ_ONLY);
        comboSport.setBounds(50, 50, 150, 65);

        comboSport.setItems(Sport.items());
        comboSport.select(getPreferenceStore().getInt(PreferenceConstants.CHART_SPORT));
        comboSport.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                getPreferenceStore().setValue(PreferenceConstants.CHART_SPORT, comboSport.getSelectionIndex());
            }
        });

        final Label head = new Label(training, SWT.NONE);
        head.setText(Messages.AllgemeinePreferencePage_Sportarten);

        running = new BooleanFieldEditor(Sport.RUNNING.getMessage(), Sport.RUNNING.getTranslated(), getFieldEditorParent());
        biking = new BooleanFieldEditor(Sport.BIKING.getMessage(), Sport.BIKING.getTranslated(), getFieldEditorParent());
        other = new BooleanFieldEditor(Sport.OTHER.getMessage(), Sport.OTHER.getTranslated(), getFieldEditorParent());
        addField(running);
        addField(biking);
        addField(other);

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
        if (source instanceof BooleanFieldEditor) {
            checkState();
        }
    }

}