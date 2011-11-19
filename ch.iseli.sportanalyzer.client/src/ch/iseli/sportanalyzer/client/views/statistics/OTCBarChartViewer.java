package ch.iseli.sportanalyzer.client.views.statistics;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.iseli.sportanalyzer.client.cache.IRecordListener;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingOverviewDatenAufbereiten;
import ch.iseli.sportanalyzer.client.charts.ChartSerieType;
import ch.iseli.sportanalyzer.client.model.SimpleTraining;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class OTCBarChartViewer implements ISelectionProvider {

    private static final String DISTANZ = "Distanz";
    private static final String HEART = "Herzfrequenz";
    private final Composite composite;
    private final JFreeChart chartDay;
    private final IntervalXYDataset dataset;
    private final ChartComposite chartComposite;
    private final TrainingCenterDataCache cache;
    private final ListenerList selectionListeners = new ListenerList(ListenerList.IDENTITY);
    private final TimeSeries distanceSerie;
    private final TimeSeries heartSerie;
    private final TimeSeriesCollection timeSeriesDistanzCollection = new TimeSeriesCollection();
    private final TimeSeriesCollection timeSeriesHeartCollection = new TimeSeriesCollection();
    private JFreeChart chart;
    private boolean withHeartRate = false;
    private final Class<? extends RegularTimePeriod> clazz;
    private final ChartSerieType type;

    public OTCBarChartViewer(Composite parent, ChartSerieType type) {
        this.type = type;
        clazz = getSeriesType(type);
        distanceSerie = new TimeSeries(DISTANZ);
        heartSerie = new TimeSeries(HEART);
        cache = TrainingCenterDataCache.getInstance();
        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(Collection<TrainingCenterDatabaseT> entry) {
                createOrUpdateDataSet(new TrainingOverviewDatenAufbereiten());
            }
        });

        composite = new Composite(parent, SWT.NONE | SWT.H_SCROLL);

        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        Button b = new Button(composite, SWT.CHECK);
        b.setText("Durchschnittliche Herzfrequenz");

        b.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
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

            private void addHeartChart() {
                final XYPlot plot = chart.getXYPlot();
                plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
                plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

                plot.setDataset(1, createOrUpdateDataSet(new TrainingOverviewDatenAufbereiten(), HEART));
                plot.mapDatasetToRangeAxis(1, 1);

                final ValueAxis axis2 = new NumberAxis("Herzfrequenz");
                plot.setRangeAxis(1, axis2);

                final XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
                plot.setRenderer(1, renderer2);
                plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
                chartComposite.forceRedraw();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        GridData gd = new GridData();
        b.setLayoutData(gd);
        dataset = createOrUpdateDataSet(new TrainingOverviewDatenAufbereiten(), DISTANZ);
        chartDay = createChart(dataset);
        chartComposite = new ChartComposite(composite, SWT.NONE, chartDay, true);
        gd = new GridData(SWT.FILL);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        chartComposite.setLayoutData(gd);
    }

    private Class<? extends RegularTimePeriod> getSeriesType(ChartSerieType type) {
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

    private void createOrUpdateDataSet(TrainingOverviewDatenAufbereiten trainingOverviewDatenAufbereiten) {
        createOrUpdateDataSet(trainingOverviewDatenAufbereiten, DISTANZ);
        createOrUpdateDataSet(trainingOverviewDatenAufbereiten, HEART);
    }

    private IntervalXYDataset createOrUpdateDataSet(TrainingOverviewDatenAufbereiten daten, String serieTyp) {
        Map<String, TimeSeries> series = updateSeries(daten);
        if (DISTANZ.equals(serieTyp)) {
            timeSeriesDistanzCollection.removeAllSeries();
            timeSeriesDistanzCollection.addSeries(series.get(DISTANZ));
            return timeSeriesDistanzCollection;
        } else if (HEART.equals(serieTyp)) {
            timeSeriesHeartCollection.removeAllSeries();
            timeSeriesHeartCollection.addSeries(series.get(HEART));
            return timeSeriesHeartCollection;
        }
        throw new IllegalArgumentException("Chart für " + serieTyp + " existiert noch nicht");
    }

    private Map<String, TimeSeries> updateSeries(TrainingOverviewDatenAufbereiten daten) {
        Map<String, TimeSeries> map = new HashMap<String, TimeSeries>();
        List<SimpleTraining> trainings = new ArrayList<SimpleTraining>();
        if (ChartSerieType.WEEK.equals(type)) {
            trainings.addAll(daten.getTrainingsPerWeek());
        }
        //
        for (SimpleTraining t : trainings) {
            RegularTimePeriod period = RegularTimePeriod.createInstance(clazz, t.getDatum(), Calendar.getInstance().getTimeZone());
            distanceSerie.addOrUpdate(period, t.getDistanzInMeter() / 1000);
            if (withHeartRate) {
                heartSerie.addOrUpdate(period, t.getAvgHeartRate());
            }
        }
        map.put(DISTANZ, distanceSerie);
        map.put(HEART, heartSerie);
        return map;
    }

    private JFreeChart createChart(IntervalXYDataset dataset) {

        chart = ChartFactory.createXYBarChart("Lauflängen", "Datum", true, "Distanz[m]", dataset, PlotOrientation.VERTICAL, false, true, false);
        chart.setAntiAlias(true);
        chart.setBorderVisible(false);
        org.eclipse.swt.graphics.Color b = Display.getDefault().getActiveShell().getBackground();
        Color paint = new Color(b.getRed(), b.getGreen(), b.getBlue());
        chart.setBackgroundPaint(paint);

        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new XYBarRenderer());

        plot.setBackgroundPaint(paint);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        OTCBarPainter painter = new OTCBarPainter();
        renderer.setBarPainter(painter);

        StandardXYToolTipGenerator generator = new StandardXYToolTipGenerator("{1} = {2}km", new SimpleDateFormat("dd.MM.yyyy"), new DecimalFormat("0.000"));
        renderer.setBaseToolTipGenerator(generator);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        axis.setLowerMargin(0.01);
        axis.setUpperMargin(0.01);
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
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        selectionListeners.add(listener);
    }

    @Override
    public ISelection getSelection() {
        // return selectedObject != null ? new StructuredSelection(selectedObject) : StructuredSelection.EMPTY;
        return null;
    }

    @Override
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        selectionListeners.remove(listener);
    }

    @Override
    public void setSelection(ISelection selection) {
        // TODO Auto-generated method stub

    }

}
