package ch.opentrainingcenter.client.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.i18n.Messages;
import static ch.opentrainingcenter.client.preferences.ChartPreferencePage.INDENT;

public class TrainingTargetPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public TrainingTargetPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
        setDescription(Messages.TrainingTargetPreferencePage0);
    }

    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {

        final Composite parent = getFieldEditorParent();
        final GridLayout gridLayout = GridLayoutFactory.swtDefaults().create();

        final Group groupTrainingTarget = new Group(parent, SWT.NONE);
        groupTrainingTarget.setText(Messages.TrainingTargetPreferencePage1);
        groupTrainingTarget.setLayout(gridLayout);
        final Composite training = new Composite(groupTrainingTarget, SWT.NONE);
        training.setLayout(GridLayoutFactory.swtDefaults().create());

        final IntegerFieldEditor weekToPlan = new IntegerFieldEditor(PreferenceConstants.WEEK_FOR_PLAN, "Wochen", training); //$NON-NLS-1$
        weekToPlan.setLabelText(Messages.TrainingTargetPreferencePage_2);
        weekToPlan.setValidRange(0, Integer.MAX_VALUE);
        addField(weekToPlan);

        addField(new ColorFieldEditor(PreferenceConstants.ZIEL_ERFUELLT_COLOR, Messages.TrainingTargetPreferencePage_4, training));
        addField(new ColorFieldEditor(PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR, Messages.TrainingTargetPreferencePage_5, training));
        addField(new ColorFieldEditor(PreferenceConstants.ZIEL_NICHT_BEKANNT_COLOR, Messages.TrainingTargetPreferencePage_6, training));

        // -- layout
        GridDataFactory.defaultsFor(groupTrainingTarget).grab(true, true).span(2, 1).indent(INDENT, INDENT).applyTo(groupTrainingTarget);
    }
}
