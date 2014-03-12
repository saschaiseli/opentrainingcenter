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

import ch.opentrainingcenter.charts.bar.OTCCategoryChartViewer;
import ch.opentrainingcenter.charts.ng.SimpleTrainingChart;
import ch.opentrainingcenter.charts.single.ChartSerieType;
import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.chart.IStatistikCreator;
import ch.opentrainingcenter.model.chart.SimpleTrainingCalculator;
import ch.opentrainingcenter.model.chart.StatistikFactory;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.model.training.filter.SimpleTrainingFilter;
import ch.opentrainingcenter.transfer.ITraining;

public class DynamicChartViewPart extends ViewPart {

    private static final Logger LOGGER = Logger.getLogger(DynamicChartViewPart.class);

    public static final String ID = "ch.opentrainingcenter.client.views.ngchart.DynamicChart"; //$NON-NLS-1$
    private FormToolkit toolkit;
    private ScrolledForm form;

    private final IDatabaseAccess databaseAccess;

    private final OTCCategoryChartViewer chartViewer;

    private Section sectionChart;

    private DateTime dateFrom;

    private DateTime dateBis;

    private Combo comboFilter;

    private Combo comboChartType;

    private Button compareWithLastYear;

    public DynamicChartViewPart() {
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        chartViewer = new OTCCategoryChartViewer(Activator.getDefault().getPreferenceStore());
    }

    @Override
    public void createPartControl(final Composite parent) {

        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);

        toolkit.decorateFormHeading(form.getForm());

        form.setText(Messages.DynamicChartViewPart_0);
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
        sectionFilter.setText(Messages.DynamicChartViewPart_1);
        sectionFilter.setDescription(Messages.DynamicChartViewPart_2);

        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        sectionFilter.setLayoutData(td);

        final Composite container = toolkit.createComposite(sectionFilter);
        GridLayoutFactory.swtDefaults().numColumns(7).applyTo(container);

        final Label lDay = new Label(container, SWT.NONE);
        lDay.setText(Messages.DynamicChartViewPart_3);
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(lDay);

        comboFilter = new Combo(container, SWT.READ_ONLY);
        comboFilter.setBounds(50, 50, 150, 65);

        comboFilter.setItems(ChartSerieType.items());
        comboFilter.select(0);
        comboFilter.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final ChartSerieType type = ChartSerieType.getByIndex(comboFilter.getSelectionIndex());
                update(type);
                compareWithLastYear.setEnabled(!ChartSerieType.DAY.equals(type));
            }

        });

        final Label y = new Label(container, SWT.NONE);
        y.setText(Messages.DynamicChartViewPart_4);
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(y);

        comboChartType = new Combo(container, SWT.READ_ONLY);
        comboChartType.setItems(SimpleTrainingChart.items());
        comboChartType.select(SimpleTrainingChart.DISTANZ.getIndex());
        comboChartType.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final ChartSerieType type = ChartSerieType.getByIndex(comboFilter.getSelectionIndex());
                update(type);
            }

        });
        final ChartSerieType cst = ChartSerieType.getByIndex(comboFilter.getSelectionIndex());

        compareWithLastYear = new Button(container, SWT.CHECK);
        compareWithLastYear.setText(Messages.DynamicChartViewPart_9);
        compareWithLastYear.setEnabled(!ChartSerieType.DAY.equals(cst));
        compareWithLastYear.setToolTipText(Messages.DynamicChartViewPart_10);
        compareWithLastYear.addSelectionListener(new UpdateSelectionAdapter());

        GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).span(3, 1).applyTo(compareWithLastYear);
        // ------- neue Zeile
        final Label vonLabel = toolkit.createLabel(container, Messages.DynamicChartViewPart_6);
        final org.joda.time.DateTime von = org.joda.time.DateTime.now().minusMonths(3);
        dateFrom = new DateTime(container, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
        dateFrom.setDate(von.getYear(), von.getMonthOfYear() - 1, von.getDayOfMonth());
        dateFrom.addSelectionListener(new UpdateSelectionAdapter());
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(vonLabel);

        final Label bisLabel = toolkit.createLabel(container, Messages.DynamicChartViewPart_7);
        final org.joda.time.DateTime bis = org.joda.time.DateTime.now();
        dateBis = new DateTime(container, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
        dateBis.setDate(bis.getYear(), bis.getMonthOfYear() - 1, bis.getDayOfMonth());
        dateBis.addSelectionListener(new UpdateSelectionAdapter());
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(bisLabel);

        sectionFilter.setClient(container);
    }

    private class UpdateSelectionAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(final SelectionEvent e) {
            final ChartSerieType type = ChartSerieType.getByIndex(comboFilter.getSelectionIndex());
            update(type);
        }
    }

    private void addChartSection(final Composite body) {
        sectionChart = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        sectionChart.setExpanded(false);
        sectionChart.setText(Messages.DynamicChartViewPart_8);

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
                final Date start = TimeHelper.getDate(dateFrom.getYear(), dateFrom.getMonth(), dateFrom.getDay());
                final Date end = TimeHelper.getDate(dateBis.getYear(), dateBis.getMonth(), dateBis.getDay());
                final List<ISimpleTraining> filteredDataNow = getFilteredData(type, start, end);
                chartViewer.createPartControl(type, SimpleTrainingChart.DISTANZ, filteredDataNow, filteredDataNow);
                sectionChart.setExpanded(true);
            }
        });
    }

    private void update(final ChartSerieType chartSerieType) {
        LOGGER.info("UPDATE CHART"); //$NON-NLS-1$
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final Date start = TimeHelper.getDate(dateFrom.getYear(), dateFrom.getMonth(), dateFrom.getDay());
                final Date end = TimeHelper.getDate(dateBis.getYear(), dateBis.getMonth(), dateBis.getDay());
                final org.joda.time.DateTime dtStart = new org.joda.time.DateTime(start.getTime());
                final org.joda.time.DateTime dtEnd = new org.joda.time.DateTime(end.getTime());
                final List<ISimpleTraining> dataPast = getFilteredData(chartSerieType, dtStart.minusYears(1).toDate(), dtEnd.minusYears(1).toDate());
                final List<ISimpleTraining> dataNow = getFilteredData(chartSerieType, start, end);
                final SimpleTrainingChart chartType = SimpleTrainingChart.getByIndex(comboChartType.getSelectionIndex());
                final boolean compareLast = compareWithLastYear.getSelection();

                chartViewer.updateData(dataPast, dataNow, chartSerieType, chartType, compareLast);
                chartViewer.updateRenderer(chartSerieType, chartType, compareLast);
                chartViewer.forceRedraw();
            }
        });
    }

    private List<ISimpleTraining> getFilteredData(final ChartSerieType chartSerieType, final Date start, final Date end) {
        final IStatistikCreator sc = StatistikFactory.createStatistik();
        final List<ITraining> allTrainings = databaseAccess.getAllTrainings(ApplicationContext.getApplicationContext().getAthlete());
        final List<ISimpleTraining> filteredData = new ArrayList<>();
        final SimpleTrainingFilter filter = new SimpleTrainingFilter(start, end, null);
        for (final ISimpleTraining tr : ModelFactory.convertToSimpleTraining(allTrainings)) {
            final ISimpleTraining st = filter.filter(tr);
            if (st != null) {
                filteredData.add(st);
            }
        }
        switch (chartSerieType) {
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
