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
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;

public class TrainingTargetPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public TrainingTargetPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
        setDescription(Messages.TrainingTargetPreferencePage_0);
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
        groupTrainingTarget.setText(Messages.TrainingTargetPreferencePage_1);
        groupTrainingTarget.setLayout(gridLayout);
        final Composite training = new Composite(groupTrainingTarget, SWT.NONE);
        training.setLayout(GridLayoutFactory.swtDefaults().create());

        final IntegerFieldEditor kmPerWeek = new IntegerFieldEditor(PreferenceConstants.KM_PER_WEEK, "KM / Woche", training); //$NON-NLS-1$
        kmPerWeek.setLabelText(Messages.TrainingTargetPreferencePage_3);
        kmPerWeek.setValidRange(0, Integer.MAX_VALUE);
        kmPerWeek.setErrorMessage(Messages.TrainingTargetPreferencePage_4);

        addField(kmPerWeek);
        addField(new ColorFieldEditor(PreferenceConstants.KM_PER_WEEK_COLOR_BELOW, Messages.TrainingTargetPreferencePage_5, training));
        addField(new ColorFieldEditor(PreferenceConstants.KM_PER_WEEK_COLOR_ABOVE, Messages.TrainingTargetPreferencePage_6, training));

        // -----------
        final Group groupChartColors = new Group(parent, SWT.NONE);
        groupChartColors.setText(Messages.TrainingTargetPreferencePage_7);
        groupChartColors.setLayout(gridLayout);
        final Composite chart = new Composite(groupChartColors, SWT.NONE);
        chart.setLayout(GridLayoutFactory.swtDefaults().create());

        addField(new ColorFieldEditor(PreferenceConstants.DISTANCE_CHART_COLOR, Messages.TrainingTargetPreferencePage_8, chart));
        addField(new ColorFieldEditor(PreferenceConstants.DISTANCE_HEART_COLOR, Messages.TrainingTargetPreferencePage_9, chart));

        GridDataFactory.defaultsFor(groupTrainingTarget).grab(false, false).span(2, 1).indent(5, 5).applyTo(groupTrainingTarget);
        GridDataFactory.defaultsFor(groupTrainingTarget).grab(true, false).span(2, 1).indent(5, 5).applyTo(groupChartColors);
    }
}
