/**
 *    OpenTrainingCenter
 *
 *    Copyright (C) 2014 Sascha Iseli sascha.iseli(at)gmx.ch
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.opentrainingcenter.client.preferences;

import static ch.opentrainingcenter.client.preferences.ChartPreferencePage.INDENT;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.i18n.Messages;

public class ChartColorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public ChartColorPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
        setDescription(Messages.ChartColorPreferencePage_0);
    }

    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        final Composite parent = getFieldEditorParent();
        final GridLayout gridLayout = GridLayoutFactory.swtDefaults().create();

        // -----------
        final Group groupChartColors = new Group(parent, SWT.NONE);
        groupChartColors.setText(Messages.TrainingTargetPreferencePage7);
        groupChartColors.setLayout(gridLayout);
        final Composite chart = new Composite(groupChartColors, SWT.NONE);
        chart.setLayout(GridLayoutFactory.swtDefaults().create());

        addField(new ColorFieldEditor(PreferenceConstants.CHART_DISTANCE_COLOR, Messages.TrainingTargetPreferencePage8, chart));
        addField(new ColorFieldEditor(PreferenceConstants.CHART_HEART_COLOR, Messages.TrainingTargetPreferencePage9, chart));

        addField(new ColorFieldEditor(PreferenceConstants.CHART_DISTANCE_COLOR_PAST, Messages.TrainingTargetPreferencePage10, chart));
        addField(new ColorFieldEditor(PreferenceConstants.CHART_HEART_COLOR_PAST, Messages.TrainingTargetPreferencePage11, chart));

        addField(new ColorFieldEditor(PreferenceConstants.CHART_COLOR_RANGE, Messages.TrainingTargetPreferencePage12, chart));

        // ----------- Vital
        final Group groupVitalColors = new Group(parent, SWT.NONE);
        groupVitalColors.setText(Messages.TrainingTargetPreferencePage7);
        groupVitalColors.setLayout(gridLayout);
        final Composite vital = new Composite(groupVitalColors, SWT.NONE);
        vital.setLayout(GridLayoutFactory.swtDefaults().create());

        addField(new ColorFieldEditor(PreferenceConstants.RUHEPULS_COLOR, Messages.TrainingTargetPreferencePage_0, vital));
        addField(new ColorFieldEditor(PreferenceConstants.GEWICHT_COLOR, Messages.TrainingTargetPreferencePage_1, vital));

        // layout
        GridDataFactory.defaultsFor(groupChartColors).grab(true, true).span(2, 1).indent(INDENT, INDENT).applyTo(groupChartColors);
        GridDataFactory.defaultsFor(groupVitalColors).grab(true, true).span(2, 1).indent(INDENT, INDENT).applyTo(groupVitalColors);
    }

}
