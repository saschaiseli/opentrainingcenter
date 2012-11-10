package ch.opentrainingcenter.client.charts;

//import static org.jfree.chart.labels.ItemLabelAnchor.*;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CustomXYToolTipGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.TextAnchor;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.cache.TrainingOverviewDatenAufbereiten;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.cache.impl.TrainingsPlanCache;
import ch.opentrainingcenter.client.charts.internal.StatistikCreator;
import ch.opentrainingcenter.client.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.model.RunType;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.tcx.ActivityT;
import static org.jfree.ui.TextAnchor.CENTER;

public class OTCBarChartViewer implements ISelectionProvider {

    private static final int ALPHA = 255;

    private static final int KILOMETER_IN_METER = 1000;

    private static final Logger LOGGER = Logger.getLogger(OTCBarChartViewer.class);

    private static final String DISTANZ = Messages.OTCBarChartViewer0;
    private static final String HEART = Messages.OTCBarChartViewer1;
    private final Composite composite;
    private final ChartComposite chartComposite;
    private final ListenerList selectionListeners = new ListenerList(ListenerList.IDENTITY);
    private final TimeSeries distanceSerie;
    private final TimeSeries heartSerie;
    private final TimeSeriesCollection timeSeriesDistanzCollection = new TimeSeriesCollection();
    private final TimeSeriesCollection timeSeriesHeartCollection = new TimeSeriesCollection();
    private boolean withHeartRate = false;
    private boolean withLabel = true;
    private final Class<? extends RegularTimePeriod> clazz;
    private final ChartSerieType type;
    private XYItemLabelGenerator labelGenerator;
    private JFreeChart chart;
    private final IStatistikCreator statistik;
    private final TrainingOverviewDatenAufbereiten daten;
    private RunType filter = RunType.NONE;

    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    private final Cache cache;

    public OTCBarChartViewer(final Composite parent, final ChartSerieType type) {
        this.type = type;
        clazz = getSeriesType(type);
        statistik = new StatistikCreator();

        daten = new TrainingOverviewDatenAufbereiten(statistik, DatabaseAccessFactory.getDatabaseAccess());
        distanceSerie = new TimeSeries(DISTANZ);
        heartSerie = new TimeSeries(HEART);
        cache = TrainingCenterDataCache.getInstance();
        cache.addListener(new IRecordListener<ActivityT>() {

            @Override
            public void recordChanged(final Collection<ActivityT> entry) {
                update();
            }

            @Override
            public void deleteRecord(final Collection<ActivityT> entry) {
                update();
            }

            private void update() {
                LOGGER.info("neue Daten, Charts aktualisieren"); //$NON-NLS-1$
                createOrUpdateDataSet(daten, filter);
            }
        });

        composite = new Composite(parent, SWT.NONE | SWT.H_SCROLL);

        final GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        final Composite radioContainer = new Composite(composite, SWT.NONE);
        final GridLayout radioLayout = new GridLayout(1, false);
        radioLayout.marginTop = 10;
        radioLayout.marginBottom = 40;
        radioContainer.setLayout(radioLayout);

        createFilterButton(radioContainer, null);
        for (final RunType runType : RunType.values()) {
            createFilterButton(radioContainer, runType);
        }

        if (type.isLabelVisible()) {
            final Button checkBoxKilometerAnzeigen = new Button(radioContainer, SWT.CHECK);
            checkBoxKilometerAnzeigen.setText(Messages.OTCBarChartViewer9);
            checkBoxKilometerAnzeigen.setSelection(true);
            checkBoxKilometerAnzeigen.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    withLabel = !withLabel;
                    final XYPlot plot = chart.getXYPlot();
                    final XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer(0);
                    if (withLabel) {
                        renderer.setBaseItemLabelGenerator(labelGenerator);
                    } else {
                        labelGenerator = renderer.getBaseItemLabelGenerator();

                        final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, -Math.PI / 2.0);
                        renderer.setBasePositiveItemLabelPosition(position);

                        renderer.setBaseItemLabelFont(new Font("Default", Font.PLAIN, 7)); //$NON-NLS-1$
                        renderer.setBaseItemLabelGenerator(null);
                    }
                    chartComposite.forceRedraw();
                }

                @Override
                public void widgetDefaultSelected(final SelectionEvent e) {
                }
            });
        }

        final Button b = new Button(radioContainer, SWT.CHECK);
        b.setText(Messages.OTCBarChartViewer2);

        b.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                withHeartRate = !withHeartRate;
                if (withHeartRate) {
                    addHeartChart();
                } else {
                    removeHeartChart();
                }
            }

            private void removeHeartChart() {
                final XYPlot plot = chart.getXYPlot();
                plot.setDataset(1, null);
                plot.setRangeAxis(1, null);
                plot.setRenderer(1, null);
                chartComposite.forceRedraw();
            }

            @SuppressWarnings("deprecation")
            private void addHeartChart() {
                final XYPlot plot = chart.getXYPlot();
                plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
                plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

                plot.setDataset(1, createOrUpdateDataSet(daten, HEART, filter));
                plot.mapDatasetToRangeAxis(1, 1);

                final ValueAxis axis2 = new NumberAxis(Messages.OTCBarChartViewer3);
                plot.setRangeAxis(1, axis2);

                final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
                renderer.setSeriesPaint(0, ColorFromPreferenceHelper.getColor(store, PreferenceConstants.DISTANCE_HEART_COLOR, 255));
                renderer.setShape(Cross.createCross());
                final XYToolTipGenerator generator = new CustomXYToolTipGenerator();
                renderer.setToolTipGenerator(generator);
                plot.setRenderer(1, renderer);

                plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
                chartComposite.forceRedraw();
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {

            }
        });

        GridData gd = new GridData();
        b.setLayoutData(gd);
        final IntervalXYDataset dataset = createOrUpdateDataSet(daten, DISTANZ, null);
        createChart(dataset, type);
        chartComposite = new ChartComposite(composite, SWT.NONE, chart, true);
        gd = new GridData(SWT.FILL);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        chartComposite.setLayoutData(gd);
    }

    private void createFilterButton(final Composite radioContainer, final RunType buttonFilter) {
        final Button button = new Button(radioContainer, SWT.RADIO);
        if (buttonFilter != null) {
            button.setText(buttonFilter.getTitle());
        } else {
            button.setText(Messages.OTCBarChartViewer10);
            button.setSelection(true);
        }
        button.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final Button source = (Button) e.getSource();
                if (source.getSelection()) {
                    filter = buttonFilter;
                    createOrUpdateDataSet(daten, filter);
                }
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {

            }
        });

    }

    private Class<? extends RegularTimePeriod> getSeriesType(final ChartSerieType chartType) {
        final Class<? extends RegularTimePeriod> thisClazz;
        switch (chartType) {
        case DAY:
            thisClazz = Day.class;
            break;
        case WEEK:
            thisClazz = Week.class;
            break;
        case MONTH:
            thisClazz = Month.class;
            break;
        case YEAR:
            thisClazz = Year.class;
            break;
        default:
            thisClazz = Day.class;
        }
        return thisClazz;
    }

    private void createOrUpdateDataSet(final TrainingOverviewDatenAufbereiten trainingDaten, final RunType runTypeFilter) {
        LOGGER.debug("createOrUpdateDataSet... " + runTypeFilter); //$NON-NLS-1$
        createOrUpdateDataSet(trainingDaten, DISTANZ, runTypeFilter);
        createOrUpdateDataSet(trainingDaten, HEART, runTypeFilter);
    }

    private IntervalXYDataset createOrUpdateDataSet(final TrainingOverviewDatenAufbereiten trainingDaten, final String serieTyp, final RunType runTypeFilter) {
        final Map<String, TimeSeries> series = updateSeries(trainingDaten, runTypeFilter);
        if (DISTANZ.equals(serieTyp)) {
            timeSeriesDistanzCollection.removeAllSeries();
            timeSeriesDistanzCollection.addSeries(series.get(DISTANZ));
            timeSeriesDistanzCollection.setXPosition(TimePeriodAnchor.MIDDLE);
            return timeSeriesDistanzCollection;
        } else if (HEART.equals(serieTyp)) {
            timeSeriesHeartCollection.removeAllSeries();
            timeSeriesHeartCollection.addSeries(series.get(HEART));
            timeSeriesHeartCollection.setXPosition(TimePeriodAnchor.MIDDLE);
            return timeSeriesHeartCollection;
        }
        throw new IllegalArgumentException(Messages.OTCBarChartViewer4 + serieTyp + Messages.OTCBarChartViewer5);
    }

    private Map<String, TimeSeries> updateSeries(final TrainingOverviewDatenAufbereiten trainingDaten, final RunType runType) {
        final Map<String, TimeSeries> map = new HashMap<String, TimeSeries>();
        final List<ISimpleTraining> trainings = new ArrayList<ISimpleTraining>();
        trainingDaten.update(runType);
        LOGGER.debug(trainingDaten);
        if (ChartSerieType.DAY.equals(type)) {
            trainings.addAll(trainingDaten.getTrainingsPerDay());
        }
        if (ChartSerieType.WEEK.equals(type)) {
            trainings.addAll(trainingDaten.getTrainingsPerWeek());
        }
        if (ChartSerieType.MONTH.equals(type)) {
            trainings.addAll(trainingDaten.getTrainingsPerMonth());
        }
        if (ChartSerieType.YEAR.equals(type)) {
            trainings.addAll(trainingDaten.getTrainingsPerYear());
        }
        //
        distanceSerie.clear();
        heartSerie.clear();
        for (final ISimpleTraining t : trainings) {
            final RegularTimePeriod period = RegularTimePeriod.createInstance(clazz, t.getDatum(), Calendar.getInstance().getTimeZone());
            distanceSerie.addOrUpdate(period, t.getDistanzInMeter() / KILOMETER_IN_METER);
            if (withHeartRate && t.getAvgHeartRate() > 0) {
                heartSerie.addOrUpdate(period, t.getAvgHeartRate());
            }
        }
        map.put(DISTANZ, distanceSerie);
        map.put(HEART, heartSerie);
        return map;
    }

    private JFreeChart createChart(final IntervalXYDataset dataset, final ChartSerieType chartSerieType) {

        chart = ChartFactory.createXYBarChart(Messages.OTCBarChartViewer6, Messages.OTCBarChartViewer7, true, Messages.OTCBarChartViewer8, dataset,
                PlotOrientation.VERTICAL, false, true, false);
        chart.setAntiAlias(true);
        chart.setBorderVisible(false);
        final org.eclipse.swt.graphics.Color b = Display.getDefault().getActiveShell().getBackground();
        final Color paint = new Color(b.getRed(), b.getGreen(), b.getBlue());
        chart.setBackgroundPaint(paint);

        final XYPlot plot = chart.getXYPlot();

        XYItemRenderer myXyBarRenderer;
        if (ChartSerieType.WEEK.equals(chartSerieType)) {
            myXyBarRenderer = new WeekXYBarRenderer(dataset, store, TrainingsPlanCache.getInstance());
        } else {
            myXyBarRenderer = new XYBarRenderer();
            myXyBarRenderer.setSeriesPaint(0, ColorFromPreferenceHelper.getColor(store, PreferenceConstants.DISTANCE_CHART_COLOR, ALPHA));
        }

        plot.setRenderer(myXyBarRenderer);
        plot.setBackgroundPaint(paint);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        final XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

        final OTCBarPainter painter = new OTCBarPainter();
        renderer.setBarPainter(painter);

        renderer.setMargin(0.1);

        if (chartSerieType.isLabelVisible()) {
            final String label = "{2}km (" + chartSerieType.getLabel() + "{1})"; //$NON-NLS-1$//$NON-NLS-2$
            final StandardXYToolTipGenerator generator = new StandardXYToolTipGenerator(label, new SimpleDateFormat(chartSerieType.getFormatPattern()),
                    new DecimalFormat("0.000")); //$NON-NLS-1$
            renderer.setBaseToolTipGenerator(generator);

            final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.CENTER, CENTER, CENTER, -Math.PI / 2.0);
            renderer.setBasePositiveItemLabelPosition(position);

            final XYItemLabelGenerator lgen = new StandardXYItemLabelGenerator(label, new SimpleDateFormat(chartSerieType.getFormatPattern()),
                    new DecimalFormat("0.000")); //$NON-NLS-1$
            renderer.setBaseItemLabelGenerator(lgen);

            renderer.setBaseItemLabelsVisible(true);
        }

        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        // axis.setDateFormatOverride(new SimpleDateFormat("w"));
        return chart;
    }

    public void dispose() {
        composite.dispose();
    }

    public boolean isDisposed() {
        return composite.isDisposed();
    }

    public Control getControl() {
        return composite;
    }

    public Display getDisplay() {
        return composite.getDisplay();
    }

    @Override
    public void addSelectionChangedListener(final ISelectionChangedListener listener) {
        selectionListeners.add(listener);
    }

    @Override
    public ISelection getSelection() {
        // return selectedObject != null ? new
        // StructuredSelection(selectedObject) : StructuredSelection.EMPTY;
        return null;
    }

    @Override
    public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
        selectionListeners.remove(listener);
    }

    @Override
    public void setSelection(final ISelection selection) {
    }

}
