package ch.iseli.sportanalyzer.client.views.statistics;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.iseli.sportanalyzer.client.cache.IRecordListener;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.model.TrainingOverview;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class StatisticView extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.views.statistics.StatisticView";
    private final TrainingCenterDataCache cache;
    private final List<TrainingOverview> all;
    private DefaultCategoryDataset dataset;
    private JFreeChart chart;
    private ChartComposite chartComposite;

    public StatisticView() {
        cache = TrainingCenterDataCache.getInstance();
        all = cache.getAllOverviews();
    }

    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout(SWT.FILL));
        TabFolder tabs = new TabFolder(container, SWT.BORDER);
        TabItem itemA = new TabItem(tabs, SWT.PUSH);
        itemA.setText("Tag");
        final Composite compositeA = new Composite(tabs, SWT.NONE);
        itemA.setControl(compositeA);

        GridLayout layout = new GridLayout(2, false);
        compositeA.setLayout(layout);
        compositeA.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));

        Button b = new Button(compositeA, SWT.PUSH);
        b.setText("draw");
        GridData gd = new GridData();
        b.setLayoutData(gd);

        dataset = createDataset();
        dataset.addChangeListener(new DatasetChangeListener() {

            @Override
            public void datasetChanged(DatasetChangeEvent arg0) {
                System.out.println("redraw chart");
                chart.fireChartChanged();
                chartComposite.forceRedraw();
            }
        });
        chart = createChart(dataset);
        chartComposite = new ChartComposite(compositeA, SWT.NONE, chart, true);
        gd = new GridData(SWT.FILL);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        chartComposite.setLayoutData(gd);

        b.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("clicked");
                // dataset = new DefaultCategoryDataset();
                dataset.addValue(Math.random(), "random" + Math.random(), "Label 11");
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        TabItem itemB = new TabItem(tabs, SWT.PUSH);
        Composite compositeB = new Composite(tabs, SWT.NONE);
        compositeB.setLayout(new FillLayout());
        new Button(compositeB, SWT.PUSH).setText("Button B");
        itemB.setText("Woche");
        itemB.setControl(compositeB);
        tabs.pack();

        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(Collection<TrainingCenterDatabaseT> entry) {
                all.clear();
                all.addAll(cache.getAllOverviews());
                chart.fireChartChanged();
            }
        });
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // row keys...
        String series1 = "First";
        String category1 = "Label 1";

        for (TrainingOverview training : all) {
            dataset.addValue(Double.valueOf(training.getLaengeInKilometer()), training.getDatum(), category1);
        }
        dataset.addValue(Math.random(), "random" + Math.random(), "Label 2");
        return dataset;
    }

    /**
     * Creates a chart.
     * 
     * @param dataset
     *            dataset.
     * 
     * @return A chart.
     */

    private JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createBarChart("Bar Chart", // chart
                // title
                "Labels", // domain axis label
                "Values", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
                );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);
        return chart;

    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
    }

}
