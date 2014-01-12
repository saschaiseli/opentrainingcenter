/**
 *    OpenTrainingCenter
 *
 *    Copyright (C) 2013 Sascha Iseli sascha.iseli(at)gmx.ch
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

package ch.opentrainingcenter.client.views.ngchart;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.charts.bar.OTCDynamicChartViewer;
import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.ChartSerieType;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.chart.SimpleTrainingCalculator;
import ch.opentrainingcenter.model.chart.StatistikCreator;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.model.training.filter.SimpleTrainingFilter;
import ch.opentrainingcenter.transfer.ITraining;

public class DynamicChartViewPart extends ViewPart {

    private static final Logger LOGGER = Logger.getLogger(DynamicChartViewPart.class);

    public static final String ID = "ch.opentrainingcenter.client.views.ngchart.DynamicChart"; //$NON-NLS-1$
    private FormToolkit toolkit;
    private ScrolledForm form;
    private final Cursor cursorHand;
    private final Cursor cursorDefault;

    private Label vonDatum;
    private Date von;
    private Label bisDatum;
    private Date bis;

    private final IDatabaseAccess databaseAccess;

    private final OTCDynamicChartViewer chartViewer;

    private Section sectionChart;

    private Button compareTraining;

    private Button showHeart;

    public DynamicChartViewPart() {
        cursorHand = new Cursor(Display.getDefault(), SWT.CURSOR_HAND);
        cursorDefault = new Cursor(Display.getDefault(), SWT.CURSOR_ARROW);
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        chartViewer = new OTCDynamicChartViewer(Activator.getDefault().getPreferenceStore());
    }

    @Override
    public void createPartControl(final Composite parent) {

        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);

        toolkit.decorateFormHeading(form.getForm());

        form.setText("Auswertungen");
        final Composite body = form.getBody();

        final TableWrapLayout layout = new TableWrapLayout();
        layout.makeColumnsEqualWidth = true;
        layout.numColumns = 1;
        body.setLayout(layout);

        addFilterSection(body);

        addChartSection(body);
    }

    private void addFilterSection(final Composite body) {
        final Section sectionFilter = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        sectionFilter.setExpanded(true);
        sectionFilter.setText("Filter");
        sectionFilter.setDescription("Hier werden die zu darstellenden Daten gefiltert.");

        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        sectionFilter.setLayoutData(td);

        final Composite container = toolkit.createComposite(sectionFilter);
        GridLayoutFactory.swtDefaults().numColumns(7).applyTo(container);

        final Label lDay = new Label(container, SWT.NONE);
        lDay.setText("X-Achse");

        final Combo comboFilter = new Combo(container, SWT.READ_ONLY);
        comboFilter.setBounds(50, 50, 150, 65);

        comboFilter.setItems(ChartSerieType.items());
        comboFilter.select(0);
        comboFilter.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final ChartSerieType type = ChartSerieType.getByIndex(comboFilter.getSelectionIndex());
                update(type, compareTraining.getSelection());
                compareTraining.setEnabled(ChartSerieType.WEEK.equals(type));
            }
        });

        compareTraining = new Button(container, SWT.CHECK);
        compareTraining.setText("Vergleich mit Planung");
        compareTraining.setEnabled(false);
        compareTraining.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final ChartSerieType type = ChartSerieType.getByIndex(comboFilter.getSelectionIndex());
                update(type, compareTraining.getSelection());
            }
        });
        GridDataFactory.swtDefaults().span(5, 1).applyTo(compareTraining);
        // -------
        toolkit.createLabel(container, "Von: ");

        final DateTime dateFrom = new DateTime(container, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
        dateFrom.setDate(2007, 0, 1);

        toolkit.createLabel(container, "Bis: ");
        final DateTime dateBis = new DateTime(container, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
        dateBis.setDate(2007, 0, 1);

        sectionFilter.setClient(container);
    }

    private void addChartSection(final Composite body) {
        sectionChart = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        sectionChart.setExpanded(false);
        sectionChart.setText("Chart");

        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        sectionChart.setLayoutData(td);

        final Composite container = toolkit.createComposite(sectionChart);
        GridLayoutFactory.swtDefaults().numColumns(1).applyTo(container);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).hint(SWT.DEFAULT, 400).applyTo(container);

        chartViewer.setParent(container);

        createPartControl(ChartSerieType.DAY);

        sectionChart.setClient(container);
    }

    private void createPartControl(final ChartSerieType type) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final List<ISimpleTraining> filteredData = getFilteredData(type);
                chartViewer.createPartControl(type, filteredData);
                sectionChart.setExpanded(true);
            }
        });
    }

    private void update(final ChartSerieType type, final boolean compare) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final List<ISimpleTraining> data = getFilteredData(type);
                chartViewer.updateData(data, type);
                chartViewer.updateRenderer(type, compare);
                chartViewer.forceRedraw();
            }
        });
    }

    private List<ISimpleTraining> getFilteredData(final ChartSerieType chartType) {
        final StatistikCreator sc = new StatistikCreator();
        final List<ITraining> allTrainings = databaseAccess.getAllTrainings(ApplicationContext.getApplicationContext().getAthlete());
        final SimpleTrainingFilter filter = new SimpleTrainingFilter(new Date(0), new Date(), null);
        final List<ISimpleTraining> filteredData = new ArrayList<>();
        for (final ISimpleTraining tr : ModelFactory.convertToSimpleTraining(allTrainings)) {
            final ISimpleTraining st = filter.filter(tr);
            if (st != null) {
                filteredData.add(st);
            }
        }
        switch (chartType) {
        case DAY:
            return sc.getTrainingsProTag(filteredData);
        case WEEK:
            return SimpleTrainingCalculator.createSum(sc.getTrainingsProWoche(filteredData), null);
        case MONTH:
            return SimpleTrainingCalculator.createSum(sc.getTrainingsProMonat(filteredData), null);
        case YEAR:
            final Map<Integer, List<ISimpleTraining>> trainingsProJahr = sc.getTrainingsProJahr(filteredData);
            final Map<Integer, Map<Integer, List<ISimpleTraining>>> tmp = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();
            tmp.put(1, trainingsProJahr);
            return SimpleTrainingCalculator.createSum(tmp, null);
        default:
            return sc.getTrainingsProTag(filteredData);
        }
    }

    @Override
    public void setFocus() {

    }

}
