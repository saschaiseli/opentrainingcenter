package ch.opentrainingcenter.charts.statistics.internal;

import java.awt.Color;
import java.awt.Paint;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.opentrainingcenter.charts.bar.internal.OTCXYBarPainter;
import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;

public abstract class VitaldatenChartViewer extends ViewPart implements ISelectionProvider, IRecordListener<ConcreteHealth> {

    private final ListenerList selectionListeners = new ListenerList(ListenerList.IDENTITY);
    private Composite composite;
    private final TimeSeriesCollection timeSeries = new TimeSeriesCollection();
    private ChartComposite chartComposite;
    private JFreeChart chart;
    private final TimeSeries serie = new TimeSeries(getTabName());
    private final AbstractCache<Integer, ConcreteHealth> cache;

    public VitaldatenChartViewer(final AbstractCache<Integer, ConcreteHealth> cache) {
        this.cache = cache;
        this.cache.addListener(this);
    }

    /**
     * Hook Methode welche den Wert aus dem Konkreten Objekt zurückgibt.
     */
    protected abstract Number getValue(ConcreteHealth health);

    /**
     * @return Beschriftung der Y Achse
     */
    protected abstract String getYAchseBeschriftung();

    /**
     * @return Der Titel vom Chart Diagram
     */
    protected abstract String getChartTitle();

    /**
     * @return Den Namen, der im Tab dargestellt werden soll.
     */
    protected abstract String getTabName();

    /**
     * @return Die Farbe für den Bar Renderer. Wird aus den Preferences gelesen
     */
    protected abstract Paint getBarColor();

    @Override
    public void addSelectionChangedListener(final ISelectionChangedListener listener) {
        selectionListeners.add(listener);
    }

    @Override
    public ISelection getSelection() {
        return null;
    }

    @Override
    public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
        selectionListeners.remove(listener);
    }

    @Override
    public void setSelection(final ISelection selection) {

    }

    public Control getControl() {
        return composite;
    }

    @Override
    public void createPartControl(final Composite parent) {
        composite = new Composite(parent, SWT.NONE);

        final GridLayout layout = new GridLayout(1, false);
        layout.marginTop = 10;
        layout.marginBottom = 40;
        composite.setLayout(layout);

        final IntervalXYDataset dataset = createOrUpdate();
        chart = createChart(dataset);
        chartComposite = new ChartComposite(composite, SWT.NONE, chart, true);

        final GridData gd = new GridData(SWT.FILL);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        chartComposite.setLayoutData(gd);
    }

    private IntervalXYDataset createOrUpdate() {
        timeSeries.removeAllSeries();
        serie.clear();
        final List<ConcreteHealth> all = cache.getAll();
        for (final ConcreteHealth health : all) {
            final TimeZone zone = Calendar.getInstance(Locale.getDefault()).getTimeZone();
            final RegularTimePeriod period = RegularTimePeriod.createInstance(Day.class, health.getDateofmeasure(), zone);
            final Number value = getValue(health);
            final TimeSeriesDataItem item = new TimeSeriesDataItem(period, value);
            serie.add(item);
        }
        timeSeries.addSeries(serie);
        return timeSeries;
    }

    private JFreeChart createChart(final IntervalXYDataset dataset) {
        chart = ChartFactory.createXYBarChart(getChartTitle(), Messages.OTCBarChartViewer7, true, getYAchseBeschriftung(), dataset, PlotOrientation.VERTICAL,
                false, true, false);
        chart.setAntiAlias(true);
        chart.setBorderVisible(false);
        final org.eclipse.swt.graphics.Color b = Display.getDefault().getActiveShell().getBackground();
        final Color paint = new Color(b.getRed(), b.getGreen(), b.getBlue());
        chart.setBackgroundPaint(paint);

        final XYPlot plot = chart.getXYPlot();

        plot.setBackgroundPaint(paint);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        final XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

        final OTCXYBarPainter painter = new OTCXYBarPainter();
        renderer.setBarPainter(painter);
        renderer.setSeriesPaint(0, getBarColor());
        renderer.setMargin(0.1);
        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        return chart;
    }

    private void updateChart() {
        createOrUpdate();
        chartComposite.forceRedraw();
    }

    @Override
    public void setFocus() {
        composite.setFocus();
    }

    @Override
    public void recordChanged(final Collection<ConcreteHealth> entry) {
        updateChart();
    }

    @Override
    public void deleteRecord(final Collection<ConcreteHealth> entry) {
        updateChart();
    }
}
