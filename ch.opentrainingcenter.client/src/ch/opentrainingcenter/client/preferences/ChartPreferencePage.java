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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.opentrainingcenter.charts.ng.SimpleTrainingChart;
import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.i18n.Messages;

public class ChartPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    static final int INDENT = 5;

    public ChartPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
        setDescription(Messages.ChartPreferencePage_0);
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
        groupTrainingTarget.setText(Messages.ChartPreferencePage_1);
        groupTrainingTarget.setLayout(gridLayout);
        final Composite training = new Composite(groupTrainingTarget, SWT.NONE);
        training.setLayout(GridLayoutFactory.swtDefaults().create());

        final Label xAxis = new Label(training, SWT.NONE);
        xAxis.setText(Messages.ChartPreferencePage_2);

        final Combo comboFilter = new Combo(training, SWT.READ_ONLY);
        comboFilter.setBounds(50, 50, 150, 65);

        comboFilter.setItems(XAxisChart.items());
        comboFilter.select(getPreferenceStore().getInt(PreferenceConstants.CHART_XAXIS_CHART));
        comboFilter.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                getPreferenceStore().setValue(PreferenceConstants.CHART_XAXIS_CHART, comboFilter.getSelectionIndex());
            }
        });

        final Label yAxis = new Label(training, SWT.NONE);
        yAxis.setText(Messages.ChartPreferencePage_3);

        final Combo comboDist = new Combo(training, SWT.READ_ONLY);
        comboDist.setBounds(50, 50, 150, 65);

        comboDist.setItems(SimpleTrainingChart.items());
        comboDist.select(getPreferenceStore().getInt(PreferenceConstants.CHART_YAXIS_CHART));
        comboDist.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                getPreferenceStore().setValue(PreferenceConstants.CHART_YAXIS_CHART, comboDist.getSelectionIndex());
            }
        });

        addField(new BooleanFieldEditor(PreferenceConstants.CHART_COMPARE, Messages.ChartPreferencePage_4, training));

        final IntegerFieldEditor anzahlWochen = new IntegerFieldEditor(PreferenceConstants.CHART_WEEKS, Messages.ChartPreferencePage_5, training);
        addField(anzahlWochen);

        // -- layout
        GridDataFactory.defaultsFor(groupTrainingTarget).grab(true, true).span(2, 1).indent(INDENT, INDENT).applyTo(groupTrainingTarget);
    }

}
