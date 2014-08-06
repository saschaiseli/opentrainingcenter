package ch.opentrainingcenter.client.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.i18n.Messages;

public class AllgemeineFarbenPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public AllgemeineFarbenPreferencePage() {
        super(GRID);
        setDescription(Messages.AllgemeineFarbenPreferencePage_AllgemeineFarben);
    }

    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        final Composite training = getFieldEditorParent();
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

}
