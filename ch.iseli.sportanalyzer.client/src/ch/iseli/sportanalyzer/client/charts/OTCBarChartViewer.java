package ch.iseli.sportanalyzer.client.charts;

import java.awt.Color;
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
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
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

import ch.iseli.sportanalyzer.client.Messages;
import ch.iseli.sportanalyzer.client.cache.IRecordListener;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.cache.TrainingOverviewDatenAufbereiten;
import ch.iseli.sportanalyzer.client.model.SimpleTraining;

public class OTCBarChartViewer implements ISelectionProvider {

    private static final Logger logger = Logger.getLogger(OTCBarChartViewer.class);

    private static final Color COLOR_DISTANCE = new Color(128, 175, 83);
    private static final Color COLOR_HEART = new Color(186, 6, 6);
    private static final String DISTANZ = Messages.OTCBarChartViewer_0;
    private static final String HEART = Messages.OTCBarChartViewer_1;
    private final Composite composite;
    private final IntervalXYDataset dataset;
    private final ChartComposite chartComposite;
    private final ListenerList selectionListeners = new ListenerList(ListenerList.IDENTITY);
    private final TimeSeries distanceSerie;
    private final TimeSeries heartSerie;
    private final TimeSeriesCollection timeSeriesDistanzCollection = new TimeSeriesCollection();
    private final TimeSeriesCollection timeSeriesHeartCollection = new TimeSeriesCollection();
    private boolean withHeartRate = false;
    private final Class<? extends RegularTimePeriod> clazz;
    private final ChartSerieType type;

    private JFreeChart chart;

    public OTCBarChartViewer(final Composite parent, final ChartSerieType type) {
        this.type = type;
        clazz = getSeriesType(type);
        distanceSerie = new TimeSeries(DISTANZ);
        heartSerie = new TimeSeries(HEART);
        TrainingCenterDataCache.getInstance().addListener(new IRecordListener() {

            @Override
            public void recordChanged(final Collection<TrainingCenterRecord> entry) {
                update();
            }

            @Override
            public void deleteRecord(final Collection<TrainingCenterRecord> entry) {
                update();
            }

            private void update() {
                logger.info("neue Daten, Charts aktualisieren"); //$NON-NLS-1$
                createOrUpdateDataSet(new TrainingOverviewDatenAufbereiten());
            }
        });

        composite = new Composite(parent, SWT.NONE | SWT.H_SCROLL);

        final GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        final Button b = new Button(composite, SWT.CHECK);
        b.setText(Messages.OTCBarChartViewer_2);

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

                plot.setDataset(1, createOrUpdateDataSet(new TrainingOverviewDatenAufbereiten(), HEART));
                plot.mapDatasetToRangeAxis(1, 1);

                final ValueAxis axis2 = new NumberAxis(Messages.OTCBarChartViewer_3);
                plot.setRangeAxis(1, axis2);

                final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
                renderer.setSeriesPaint(0, COLOR_HEART);
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
        dataset = createOrUpdateDataSet(new TrainingOverviewDatenAufbereiten(), DISTANZ);
        createChart(dataset, type);
        chartComposite = new ChartComposite(composite, SWT.NONE, chart, true);
        gd = new GridData(SWT.FILL);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        chartComposite.setLayoutData(gd);
    }

    private Class<? extends RegularTimePeriod> getSeriesType(final ChartSerieType type) {
        final Class<? extends RegularTimePeriod> clazz;
        switch (type) {
        case DAY:
            clazz = Day.class;
            break;
        case WEEK:
            clazz = Week.class;
            break;
        case MONTH:
            clazz = Month.class;
            break;
        case YEAR:
            clazz = Year.class;
            break;
        default:
            clazz = Day.class;
        }
        return clazz;
    }

    private void createOrUpdateDataSet(final TrainingOverviewDatenAufbereiten trainingOverviewDatenAufbereiten) {
        createOrUpdateDataSet(trainingOverviewDatenAufbereiten, DISTANZ);
        createOrUpdateDataSet(trainingOverviewDatenAufbereiten, HEART);
    }

    private IntervalXYDataset createOrUpdateDataSet(final TrainingOverviewDatenAufbereiten daten, final String serieTyp) {
        final Map<String, TimeSeries> series = updateSeries(daten);
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
        throw new IllegalArgumentException(Messages.OTCBarChartViewer_4 + serieTyp + Messages.OTCBarChartViewer_5);
    }

    private Map<String, TimeSeries> updateSeries(final TrainingOverviewDatenAufbereiten daten) {
        final Map<String, TimeSeries> map = new HashMap<String, TimeSeries>();
        final List<SimpleTraining> trainings = new ArrayList<SimpleTraining>();
        if (ChartSerieType.DAY.equals(type)) {
            trainings.addAll(daten.getTrainingsPerDay());
        }
        if (ChartSerieType.WEEK.equals(type)) {
            trainings.addAll(daten.getTrainingsPerWeek());
        }
        if (ChartSerieType.MONTH.equals(type)) {
            trainings.addAll(daten.getTrainingsPerMonth());
        }
        if (ChartSerieType.YEAR.equals(type)) {
            trainings.addAll(daten.getTrainingsPerYear());
        }
        //
        for (final SimpleTraining t : trainings) {
            final RegularTimePeriod period = RegularTimePeriod.createInstance(clazz, t.getDatum(), Calendar.getInstance().getTimeZone());
            distanceSerie.addOrUpdate(period, t.getDistanzInMeter() / 1000);
            if (withHeartRate && t.getAvgHeartRate() > 0) {
                heartSerie.addOrUpdate(period, t.getAvgHeartRate());
            }
        }
        map.put(DISTANZ, distanceSerie);
        map.put(HEART, heartSerie);
        return map;
    }

    private JFreeChart createChart(final IntervalXYDataset dataset, final ChartSerieType type) {

        chart = ChartFactory.createXYBarChart(Messages.OTCBarChartViewer_6, Messages.OTCBarChartViewer_7, true, Messages.OTCBarChartViewer_8, dataset,
                PlotOrientation.VERTICAL, false, true, false);
        chart.setAntiAlias(true);
        chart.setBorderVisible(false);
        final org.eclipse.swt.graphics.Color b = Display.getDefault().getActiveShell().getBackground();
        final Color paint = new Color(b.getRed(), b.getGreen(), b.getBlue());
        chart.setBackgroundPaint(paint);

        final XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new XYBarRenderer());

        plot.setBackgroundPaint(paint);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        final XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, COLOR_DISTANCE);

        final OTCBarPainter painter = new OTCBarPainter();
        renderer.setBarPainter(painter);

        renderer.setMargin(0.1);

        if (type.isLabelVisible()) {
            final String formatString = "{2}km (" + type.getLabel() + "{1})"; //$NON-NLS-1$//$NON-NLS-2$
            final StandardXYToolTipGenerator generator = new StandardXYToolTipGenerator(formatString, new SimpleDateFormat(type.getFormatPattern()),
                    new DecimalFormat("0.000")); //$NON-NLS-1$
            renderer.setBaseToolTipGenerator(generator);

            final XYItemLabelGenerator labelGenerator = new StandardXYItemLabelGenerator(formatString, new SimpleDateFormat(type.getFormatPattern()),
                    new DecimalFormat("0.000")); //$NON-NLS-1$
            renderer.setBaseItemLabelGenerator(labelGenerator);
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
        // return selectedObject != null ? new StructuredSelection(selectedObject) : StructuredSelection.EMPTY;
        return null;
    }

    @Override
    public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
        selectionListeners.remove(listener);
    }

    @Override
    public void setSelection(final ISelection selection) {
        // TODO Auto-generated method stub

    }

}
