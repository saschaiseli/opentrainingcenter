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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import ch.opentrainingcenter.charts.bar.OTCCategoryChartViewer;
import ch.opentrainingcenter.charts.ng.TrainingChart;
import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.ui.datepicker.DateWidget;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.Event;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.data.SimplePair;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.chart.IStatistikCreator;
import ch.opentrainingcenter.model.chart.StatistikFactory;
import ch.opentrainingcenter.model.chart.TrainingCalculator;
import ch.opentrainingcenter.model.training.filter.Filter;
import ch.opentrainingcenter.model.training.filter.FilterFactory;
import ch.opentrainingcenter.model.training.filter.TrainingFilter;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class DynamicChartViewPart extends ViewPart implements IRecordListener<ITraining> {

    private static final String PFEIL = " -----> "; //$NON-NLS-1$
    public static final String ID = "ch.opentrainingcenter.client.views.ngchart.DynamicChart"; //$NON-NLS-1$
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

    private static final String[] NEW_SHORT_WEEKDAYS = new String[] { "", Messages.DynamicChartViewPart_Montag, // //$NON-NLS-1$
        Messages.DynamicChartViewPart_Dienstag, Messages.DynamicChartViewPart_Mittwoch, //
        Messages.DynamicChartViewPart_Donnerstag, Messages.DynamicChartViewPart_Freitag, //
        Messages.DynamicChartViewPart_Samstag, Messages.DynamicChartViewPart_Sonntag };

    private static final Logger LOGGER = Logger.getLogger(DynamicChartViewPart.class);

    private FormToolkit toolkit;
    private ScrolledForm form;

    private final IDatabaseAccess databaseAccess;
    private final IPreferenceStore store;
    private final OTCCategoryChartViewer chartViewer;

    private Section sectionChart;
    private Section sectionLegende;

    private DateWidget dateVon;
    private DateWidget dateBis;

    private Combo comboFilter;
    private Combo comboChartType;
    private Button compareWithLastYear;
    private Label labelIconPastPast;
    private Label labelTextPastPast;
    private Label labelIconPast;
    private Label labelTextPast;
    private Label labelIconNow;
    private Label labelTextNow;
    private Combo comboSport;
    private org.joda.time.DateTime von;

    public DynamicChartViewPart() {
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        chartViewer = new OTCCategoryChartViewer(Activator.getDefault().getPreferenceStore());
        TrainingCache.getInstance().addListener(this);
        store = Activator.getDefault().getPreferenceStore();
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

        addLegendeSection(body);

        update();
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

        comboFilter.setItems(XAxisChart.items());
        comboFilter.select(store.getInt(PreferenceConstants.CHART_XAXIS_CHART));
        comboFilter.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final XAxisChart type = XAxisChart.getByIndex(comboFilter.getSelectionIndex());
                compareWithLastYear.setEnabled(!XAxisChart.DAY.equals(type));
                // update();
                final SimplePair<Date> startEnd = getStartEnd();
                dateVon.setDate(new DateTime(startEnd.getFirst().getTime()));
                dateBis.setDate(new DateTime(startEnd.getSecond().getTime()));
            }

        });

        final Label y = new Label(container, SWT.NONE);
        y.setText(Messages.DynamicChartViewPart_4);
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(y);

        comboChartType = new Combo(container, SWT.READ_ONLY);
        comboChartType.setItems(TrainingChart.items());
        comboChartType.select(store.getInt(PreferenceConstants.CHART_YAXIS_CHART));
        comboChartType.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                update();
            }
        });
        final XAxisChart cst = XAxisChart.getByIndex(comboFilter.getSelectionIndex());

        compareWithLastYear = new Button(container, SWT.CHECK);
        compareWithLastYear.setText(Messages.DynamicChartViewPart_9);
        compareWithLastYear.setEnabled(!XAxisChart.DAY.equals(cst));
        compareWithLastYear.setSelection(store.getBoolean(PreferenceConstants.CHART_COMPARE));
        compareWithLastYear.setToolTipText(Messages.DynamicChartViewPart_10);
        compareWithLastYear.addSelectionListener(new UpdateSelectionAdapter());

        GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).span(3, 1).applyTo(compareWithLastYear);
        // ------- neue Zeile
        final Label vonLabel = toolkit.createLabel(container, Messages.DynamicChartViewPart_6);
        von = org.joda.time.DateTime.now().minusWeeks(store.getInt(PreferenceConstants.CHART_WEEKS));

        final DateFormatSymbols symbols = DateFormatSymbols.getInstance(Locale.getDefault());
        symbols.setShortWeekdays(NEW_SHORT_WEEKDAYS);

        dateVon = new DateWidget(container, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
        dateVon.setDate(von);
        dateVon.addSelectionListener(new UpdateSelectionAdapter());
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(vonLabel);

        final Label bisLabel = toolkit.createLabel(container, Messages.DynamicChartViewPart_7);

        dateBis = new DateWidget(container, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
        dateBis.setDate(DateTime.now());
        dateBis.addSelectionListener(new UpdateSelectionAdapter());
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(bisLabel);

        comboSport = new Combo(container, SWT.READ_ONLY);
        comboSport.setItems(Sport.items());
        comboSport.select(store.getInt(PreferenceConstants.CHART_SPORT));
        comboSport.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                update();
            }

        });

        sectionFilter.setClient(container);
    }

    private class UpdateSelectionAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(final SelectionEvent e) {
            update();
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
        chartViewer.createPartControl();

        sectionChart.setClient(container);
    }

    private void addLegendeSection(final Composite body) {
        sectionLegende = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        sectionLegende.setExpanded(false);
        sectionLegende.setText(Messages.DynamicChartViewPart_Legende);

        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        sectionLegende.setLayoutData(td);

        final Composite container = toolkit.createComposite(sectionLegende);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(container);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).hint(SWT.DEFAULT, 400).applyTo(container);

        labelIconNow = new Label(container, SWT.NONE);
        labelTextNow = new Label(container, SWT.NONE);

        labelIconPast = new Label(container, SWT.NONE);
        labelIconPast.setVisible(false);

        labelTextPast = new Label(container, SWT.NONE);
        labelTextPast.setVisible(false);

        labelIconPastPast = new Label(container, SWT.NONE);
        labelIconPastPast.setVisible(false);

        labelTextPastPast = new Label(container, SWT.NONE);
        labelTextPastPast.setVisible(false);

        sectionLegende.setClient(container);
    }

    private Image createImage(final Color color) {
        final Display display = PlatformUI.getWorkbench().getDisplay();
        final Image image = new Image(display, 32, 32);
        final GC gc = new GC(image);

        gc.setBackground(color);
        gc.fillRectangle(new Rectangle(0, 0, 32, 32));
        gc.dispose();

        return image;
    }

    private void update() {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final XAxisChart xAxis = XAxisChart.getByIndex(comboFilter.getSelectionIndex());
                final SimplePair<Date> startEnd = getStartEnd();
                LOGGER.info(String.format("Chart %s von %s bis %s", xAxis, startEnd.getFirst(), startEnd.getSecond())); //$NON-NLS-1$

                final int sportIndex = comboSport.getSelectionIndex();
                final Sport sport = Sport.getByIndex(sportIndex);

                final List<ITraining> dataNow = getFilteredData(xAxis, startEnd.getFirst(), startEnd.getSecond(), sport);

                final DateTime dtStartCurrent = new DateTime(startEnd.getFirst().getTime());
                final DateTime dtEndCurrent = new DateTime(startEnd.getSecond().getTime());

                final Date startPastOne = dtStartCurrent.minusYears(1).toDate();
                final Date endPastOne = dtEndCurrent.minusYears(1).toDate();
                final List<ITraining> dataPastOne = getFilteredData(xAxis, startPastOne, endPastOne, sport);

                final Date startPastTwo = dtStartCurrent.minusYears(2).toDate();
                final Date endPastTwo = dtEndCurrent.minusYears(2).toDate();
                final List<ITraining> dataPastTwo = getFilteredData(xAxis, startPastTwo, endPastTwo, sport);

                final List<List<ITraining>> past = new ArrayList<>();
                past.add(dataPastTwo);
                past.add(dataPastOne);

                final TrainingChart chartType = TrainingChart.getByIndex(comboChartType.getSelectionIndex());
                final boolean compareLast = compareWithLastYear.getSelection();

                chartViewer.updateData(dataNow, past, xAxis, chartType, compareLast);
                chartViewer.updateRenderer(xAxis, chartType, compareLast);
                chartViewer.forceRedraw();
                sectionChart.setExpanded(true);
                final java.awt.Color color;
                if (TrainingChart.DISTANZ.equals(chartType)) {
                    color = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.CHART_DISTANCE_COLOR, 255);
                } else {
                    color = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.CHART_HEART_COLOR, 255);
                }

                labelIconNow.setImage(createImage(ColorFromPreferenceHelper.awtColor2swtColot(color)));
                labelIconPast.setImage(createImage(ColorFromPreferenceHelper.awtColor2swtColot(color.brighter())));
                labelIconPastPast.setImage(createImage(ColorFromPreferenceHelper.awtColor2swtColot(color.brighter().brighter())));

                labelTextNow.setText(DATE_FORMAT.format(dtStartCurrent.toDate()) + PFEIL + DATE_FORMAT.format(dtEndCurrent.toDate()));
                labelTextPast.setText(DATE_FORMAT.format(startPastOne) + PFEIL + DATE_FORMAT.format(endPastOne));
                labelTextPastPast.setText(DATE_FORMAT.format(startPastTwo) + PFEIL + DATE_FORMAT.format(endPastTwo));

                labelIconPast.setVisible(compareLast);
                labelTextPast.setVisible(compareLast);
                labelIconPastPast.setVisible(compareLast);
                labelTextPastPast.setVisible(compareLast);

                sectionLegende.setExpanded(true);
            }

        });
    }

    private SimplePair<Date> getStartEnd() {
        final XAxisChart xAxis = XAxisChart.getByIndex(comboFilter.getSelectionIndex());
        final boolean year = XAxisChart.YEAR.equals(xAxis);
        final boolean yearTillNow = XAxisChart.YEAR_START_TILL_NOW.equals(xAxis);
        Date start = von.toDate();
        Date end = DateTime.now().toDate();
        dateVon.setEnabled(!(yearTillNow || year));
        dateBis.setEnabled(!year);
        if (yearTillNow || year) {
            final int yStart = new DateTime(end.getTime()).get(DateTimeFieldType.year());
            start = TimeHelper.getDate(yStart, 0, 1);
            dateVon.setEnabled(false);
        }
        if (year) {
            final int yEnd = new DateTime(end.getTime()).get(DateTimeFieldType.year());
            end = TimeHelper.getDate(yEnd, 11, 31);
            dateBis.setEnabled(false);
        }
        return new SimplePair<Date>(start, end);
    }

    private List<ITraining> getFilteredData(final XAxisChart chartSerieType, final Date start, final Date end, final Sport sport) {
        final IStatistikCreator sc = StatistikFactory.createStatistik();
        final List<ITraining> allTrainings = databaseAccess.getAllTrainings(ApplicationContext.getApplicationContext().getAthlete());
        final List<ITraining> filteredData = new ArrayList<>();
        final List<Filter<ITraining>> filters = new ArrayList<>();
        filters.add(FilterFactory.createFilterByDate(start, end));
        filters.add(FilterFactory.createFilterBySport(sport));
        final TrainingFilter filter = new TrainingFilter(filters);
        for (final ITraining training : allTrainings) {
            final boolean st = filter.select(training);
            if (st) {
                filteredData.add(training);
            }
        }
        switch (chartSerieType) {
        case DAY:
            return sc.getTrainingsProTag(filteredData);
        case WEEK:
            return TrainingCalculator.createSum(sc.getTrainingsProWoche(filteredData), null);
        case MONTH:
            return TrainingCalculator.createSum(sc.getTrainingsProMonat(filteredData), null);
        case YEAR:
            final Map<Integer, List<ITraining>> trainingsProJahr = sc.getTrainingsProJahr(filteredData);
            final Map<Integer, Map<Integer, List<ITraining>>> tmp = new HashMap<Integer, Map<Integer, List<ITraining>>>();
            tmp.put(1, trainingsProJahr);
            return TrainingCalculator.createSum(tmp, null);
        default:
            return sc.getTrainingsProTag(filteredData);
        }
    }

    @Override
    public void setFocus() {
        if (dateVon != null) {
            dateVon.setFocus();
        }
    }

    @Override
    public void dispose() {
        TrainingCache.getInstance().removeListener(this);
        dateVon.dispose();
        dateBis.dispose();
        super.dispose();
    }

    @Override
    public void onEvent(final Collection<ITraining> entry, final Event event) {
        update();
    }

}
