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

        addField(new ColorFieldEditor(PreferenceConstants.ZIEL_ERFUELLT_COLOR, "Ziel erreicht", training));
        addField(new ColorFieldEditor(PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR, "Ziel nicht erreicht", training));
        addField(new ColorFieldEditor(PreferenceConstants.ZIEL_NICHT_BEKANNT_COLOR, "Ziel unbekannt", training));

        // -----------
        final Group groupChartColors = new Group(parent, SWT.NONE);
        groupChartColors.setText(Messages.TrainingTargetPreferencePage7);
        groupChartColors.setLayout(gridLayout);
        final Composite chart = new Composite(groupChartColors, SWT.NONE);
        chart.setLayout(GridLayoutFactory.swtDefaults().create());

        addField(new ColorFieldEditor(PreferenceConstants.DISTANCE_CHART_COLOR, Messages.TrainingTargetPreferencePage8, chart));
        addField(new ColorFieldEditor(PreferenceConstants.DISTANCE_HEART_COLOR, Messages.TrainingTargetPreferencePage9, chart));

        // ----------- Vital
        final Group groupVitalColors = new Group(parent, SWT.NONE);
        groupVitalColors.setText(Messages.TrainingTargetPreferencePage7);
        groupVitalColors.setLayout(gridLayout);
        final Composite vital = new Composite(groupVitalColors, SWT.NONE);
        vital.setLayout(GridLayoutFactory.swtDefaults().create());

        addField(new ColorFieldEditor(PreferenceConstants.RUHEPULS_COLOR, Messages.TrainingTargetPreferencePage_0, vital));
        addField(new ColorFieldEditor(PreferenceConstants.GEWICHT_COLOR, Messages.TrainingTargetPreferencePage_1, vital));

        // -- layout
        GridDataFactory.defaultsFor(groupTrainingTarget).grab(true, true).span(2, 1).indent(5, 5).applyTo(groupTrainingTarget);
        GridDataFactory.defaultsFor(groupChartColors).grab(true, true).span(2, 1).indent(5, 5).applyTo(groupChartColors);
        GridDataFactory.defaultsFor(groupVitalColors).grab(true, true).span(2, 1).indent(5, 5).applyTo(groupVitalColors);
    }
}
