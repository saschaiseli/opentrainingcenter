package ch.iseli.sportanalyzer.client.views.statistics;

import java.awt.Color;
import java.awt.Paint;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.iseli.sportanalyzer.client.cache.IRecordListener;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingOverviewDatenAufbereiten;
import ch.iseli.sportanalyzer.client.model.SimpleTraining;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class StatisticView extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.views.statistics.StatisticView";
    private final TrainingCenterDataCache cache;
    private IntervalXYDataset datasetTag;
    private IntervalXYDataset datasetWoche;
    private JFreeChart chartDay, chartWoche;
    private ChartComposite chartComposite, chartWocheComposite;
    private final TrainingOverviewDatenAufbereiten daten;

    public StatisticView() {
        cache = TrainingCenterDataCache.getInstance();
        daten = new TrainingOverviewDatenAufbereiten();
    }

    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout(SWT.FILL));
        TabFolder tabs = new TabFolder(container, SWT.BORDER);
        TabItem itemA = new TabItem(tabs, SWT.PUSH);
        itemA.setText("Tag");
        final Composite compositeDay = new Composite(tabs, SWT.NONE);
        itemA.setControl(compositeDay);

        GridLayout layout = new GridLayout(2, false);
        compositeDay.setLayout(layout);
        compositeDay.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));

        Button b = new Button(compositeDay, SWT.PUSH);
        b.setText("draw");
        GridData gd = new GridData();
        b.setLayoutData(gd);

        datasetTag = createDataset(Day.class, cache.getAllSimpleTrainings());
        datasetTag.addChangeListener(new DatasetChangeListener() {

            @Override
            public void datasetChanged(DatasetChangeEvent arg0) {
                System.out.println("redraw chart");
                chartDay.fireChartChanged();
                chartComposite.forceRedraw();
            }
        });
        chartDay = createChart(datasetTag);
        chartComposite = new ChartComposite(compositeDay, SWT.NONE, chartDay, true);
        gd = new GridData(SWT.FILL);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        chartComposite.setLayoutData(gd);
        //
        // wochen tab
        //
        TabItem itemB = new TabItem(tabs, SWT.PUSH);
        Composite compositeWoche = new Composite(tabs, SWT.NONE);
        compositeWoche.setLayout(new FillLayout());
        itemB.setText("Woche");
        itemB.setControl(compositeWoche);

        GridLayout layoutWoche = new GridLayout(2, false);
        compositeWoche.setLayout(layoutWoche);
        compositeWoche.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));

        Button bw = new Button(compositeWoche, SWT.PUSH);
        bw.setText("draw");
        gd = new GridData();
        bw.setLayoutData(gd);

        datasetWoche = createDataset(Week.class, daten.getTrainingsPerWeek());

        chartWoche = createChart(datasetWoche);
        chartWocheComposite = new ChartComposite(compositeWoche, SWT.NONE, chartWoche, true);
        gd = new GridData(SWT.FILL);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        chartWocheComposite.setLayoutData(gd);

        cache.addListener(new IRecordListener() {
            @Override
            public void recordChanged(Collection<TrainingCenterDatabaseT> entry) {

            }
        });
    }

    private IntervalXYDataset createDataset(Class<? extends RegularTimePeriod> clazz, List<SimpleTraining> data) {
        TimeSeries t1 = new TimeSeries("");
        for (SimpleTraining training : data) {
            RegularTimePeriod period = RegularTimePeriod.createInstance(clazz, training.getDatum(), Calendar.getInstance().getTimeZone());
            t1.addOrUpdate(period, training.getDistanzInMeter() / 1000);
        }
        TimeSeriesCollection tsc = new TimeSeriesCollection(t1);
        return tsc;
    }

    /**
     * Creates a chart.
     * 
     * @param dataset
     * 
     * @param dataset
     *            dataset.
     * 
     * @return A chart.
     */

    private JFreeChart createChart(IntervalXYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYBarChart("Lauflängen", "datum", true, "distanz", dataset, PlotOrientation.VERTICAL, false, true, false);

        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new ClusteredXYBarRenderer());
        XYItemRenderer renderer = plot.getRenderer();

        StandardXYToolTipGenerator generator = new StandardXYToolTipGenerator("{1} = {2}", new SimpleDateFormat("dd.MM.yyyy"), new DecimalFormat("0.000"));
        renderer.setBaseToolTipGenerator(generator);

        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        axis.setLowerMargin(0.01);
        axis.setUpperMargin(0.01);
        return chart;

    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
    }

    class OTCBarRenderer extends LayeredBarRenderer {

        /**
         * Creates a new renderer.
         * 
         * @param colors
         *            the colors.
         */
        public OTCBarRenderer() {
        }

        /**
         * Returns the paint for an item. Overrides the default behaviour inherited from AbstractSeriesRenderer.
         * 
         * @param row
         *            the series.
         * @param column
         *            the category.
         * 
         * @return The item color.
         */
        @Override
        public Paint getItemPaint(final int row, final int column) {
            return Color.lightGray;
        }

    }
}
