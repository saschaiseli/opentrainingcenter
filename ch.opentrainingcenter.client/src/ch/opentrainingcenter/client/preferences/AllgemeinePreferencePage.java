package ch.opentrainingcenter.client.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
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
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.Sport;

public class AllgemeinePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private final List<IShoe> shoes;

    private final IDatabaseAccess databaseAccess;

    private BooleanFieldEditor running;
    private BooleanFieldEditor biking;
    private BooleanFieldEditor other;

    private ComboFieldEditor combo;

    public AllgemeinePreferencePage() {
        super(GRID);
        setDescription(Messages.SamplePreferencePage_1);
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        if (DBSTATE.OK.equals(ApplicationContext.getApplicationContext().getDbState().getState())) {
            shoes = databaseAccess.getShoes(ApplicationContext.getApplicationContext().getAthlete());
        } else {
            shoes = Collections.emptyList();
        }
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    @Override
    public void createFieldEditors() {
        final Composite parent = getFieldEditorParent();

        final Label sport = new Label(parent, SWT.NONE);
        sport.setText(Messages.ChartPreferencePage_6);

        final Combo comboSport = new Combo(parent, SWT.READ_ONLY);
        comboSport.setBounds(50, 50, 150, 65);

        comboSport.setItems(Sport.items());
        comboSport.select(getPreferenceStore().getInt(PreferenceConstants.CHART_SPORT));
        comboSport.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                getPreferenceStore().setValue(PreferenceConstants.CHART_SPORT, comboSport.getSelectionIndex());
            }
        });

        final Label head = new Label(parent, SWT.NONE);
        head.setText(Messages.AllgemeinePreferencePage_Sportarten);

        running = new BooleanFieldEditor(Sport.RUNNING.getMessage(), Sport.RUNNING.getTranslated(), parent);
        biking = new BooleanFieldEditor(Sport.BIKING.getMessage(), Sport.BIKING.getTranslated(), parent);
        other = new BooleanFieldEditor(Sport.OTHER.getMessage(), Sport.OTHER.getTranslated(), parent);
        addField(running);
        addField(biking);
        addField(other);

        final Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(separator);

        addField(new BooleanFieldEditor(PreferenceConstants.SYNTH_RUNDEN, Messages.AllgemeinePreferencePage_SYNTH_RUNDEN_ANZEIGEN, parent));

        // -----------------------------------------------
        final Label separator2 = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(separator2);

        final Label schuheLabel = new Label(parent, SWT.NONE);
        schuheLabel.setText(Messages.AllgemeinePreferencePage_DefaultSchuh);
        GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(schuheLabel);

        final List<String[]> vals = new ArrayList<String[]>();
        for (final IShoe shoe : shoes) {
            vals.add(new String[] { shoe.getSchuhname(), String.valueOf(shoe.getId()) });
        }
        combo = new ComboFieldEditor(PreferenceConstants.DEFAULT_SCHUH_1, Messages.AllgemeinePreferencePage_1_Wahl, vals.toArray(new String[0][0]), parent);
        addField(combo);
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