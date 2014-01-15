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

package ch.opentrainingcenter.charts.bar;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.TextAnchor;

import ch.opentrainingcenter.charts.bar.internal.OTCBarPainter;
import ch.opentrainingcenter.charts.bar.internal.WeekXYBarRenderer;
import ch.opentrainingcenter.charts.ng.SimpleTrainingChart;
import ch.opentrainingcenter.charts.single.ChartSerieType;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.model.cache.TrainingsPlanCache;
import ch.opentrainingcenter.model.training.ISimpleTraining;

public class OTCDynamicChartViewer {

    private static final String DECIMAL = "0.000"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(OTCDynamicChartViewer.class);

    private static final int KILOMETER_IN_METER = 1000;
    static final int ALPHA = 255;

    private ChartComposite chartComposite;

    private final TimeSeries serie = new TimeSeries("TimeChart"); //$NON-NLS-1$

    private JFreeChart chart;

    private final IPreferenceStore store;

    private Composite parent;

    private final TimeSeriesCollection dataset = new TimeSeriesCollection();

    private XYBarRenderer renderer;

    public OTCDynamicChartViewer(final IPreferenceStore store) {
        this.store = store;
    }

    public void setParent(final Composite parent) {
        this.parent = parent;
    }

    public void createPartControl(final ChartSerieType chartSerieType, final SimpleTrainingChart chartType, final List<ISimpleTraining> data) {

        init(data, chartSerieType);

        createChart(chartSerieType, chartType);

        chartComposite = new ChartComposite(parent, SWT.NONE, chart, true);
        final GridData gd = new GridData(SWT.FILL);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.heightHint = 700;
        chartComposite.setLayoutData(gd);
        chartComposite.forceRedraw();
    }

    JFreeChart createChart(final ChartSerieType type, final SimpleTrainingChart chartType) {
        chart = ChartFactory.createXYBarChart(chartType.getTitle(), chartType.getxAchse(), true, chartType.getyAchse(), dataset, PlotOrientation.VERTICAL,
                false, true, false);
        chart.setAntiAlias(true);
        chart.setBorderVisible(false);
        chart.setAntiAlias(true);
        chart.setBorderVisible(true);
        chart.setTextAntiAlias(true);
        chart.setBackgroundPaint(Color.white);
        chart.setBorderPaint(Color.white);

        updateRenderer(type, false);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainCrosshairVisible(true);
        plot.setDomainCrosshairLockedOnData(true);
        plot.setRangeCrosshairVisible(true);
        plot.setRangeCrosshairLockedOnData(true);

        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        return chart;
    }

    /**
     * Aktualisiert die Daten der Timeserie
     */
    public void updateData(final List<ISimpleTraining> data, final ChartSerieType type, final SimpleTrainingChart chartType) {

        updateAxis(chartType);

        serie.clear();
        final Class<? extends RegularTimePeriod> clazz = getSeriesType(type);
        for (final ISimpleTraining t : data) {
            final RegularTimePeriod period = RegularTimePeriod.createInstance(clazz, t.getDatum(), Calendar.getInstance().getTimeZone());
            if (SimpleTrainingChart.DISTANZ.equals(chartType)) {
                serie.addOrUpdate(period, t.getDistanzInMeter() / KILOMETER_IN_METER);
            } else if (SimpleTrainingChart.HERZ.equals(chartType)) {
                serie.addOrUpdate(period, t.getAvgHeartRate());
            } else {
                LOGGER.error(String.format("Not supported ChartType %s", chartType)); //$NON-NLS-1$
            }
        }
    }

    void updateAxis(final SimpleTrainingChart chartType) {
        if (chart != null) {
            // update axis & title
            chart.setTitle(chartType.getTitle());
            chart.getXYPlot().getRangeAxis().setLabel(chartType.getyAchse());
        }
    }

    void init(final List<ISimpleTraining> data, final ChartSerieType type) {
        updateData(data, type, SimpleTrainingChart.DISTANZ);
        dataset.removeAllSeries();
        dataset.addSeries(serie);
        dataset.setXPosition(TimePeriodAnchor.MIDDLE);
    }

    public void updateRenderer(final ChartSerieType type, final boolean compare) {
        if (ChartSerieType.WEEK.equals(type) && compare) {
            renderer = new WeekXYBarRenderer(dataset, store, TrainingsPlanCache.getInstance());
        } else {
            renderer = new XYBarRenderer();
        }
        renderer.setSeriesPaint(0, ColorFromPreferenceHelper.getColor(store, PreferenceConstants.DISTANCE_CHART_COLOR, ALPHA));

        final OTCBarPainter painter = new OTCBarPainter();
        renderer.setBarPainter(painter);

        renderer.setMargin(0.1);

        if (type.isLabelVisible()) {
            final String label = "{2}km (" + type.getLabel() + "{1})"; //$NON-NLS-1$//$NON-NLS-2$
            final SimpleDateFormat datePattern = new SimpleDateFormat(type.getFormatPattern());
            final StandardXYToolTipGenerator generator = new StandardXYToolTipGenerator(label, datePattern, new DecimalFormat(DECIMAL));
            renderer.setBaseToolTipGenerator(generator);
            renderer.setBaseItemLabelFont(new Font("Default", Font.PLAIN, 7)); //$NON-NLS-1$
            renderer.setBaseItemLabelGenerator(null);

            final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, -Math.PI / 2.0);
            renderer.setBasePositiveItemLabelPosition(position);

            final XYItemLabelGenerator lgen = new StandardXYItemLabelGenerator(label, datePattern, new DecimalFormat(DECIMAL));
            renderer.setBaseItemLabelGenerator(lgen);
            renderer.setBaseItemLabelsVisible(true);
        }
        if (chart != null) {
            chart.getXYPlot().setRenderer(renderer);
        }
    }

    Class<? extends RegularTimePeriod> getSeriesType(final ChartSerieType chartType) {
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

    public void forceRedraw() {
        LOGGER.info("Force Redraw Chart"); //$NON-NLS-1$
        chartComposite.forceRedraw();
    }

    /**
     * fuer Testzwecke
     */
    TimeSeries getSerie() {
        return serie;
    }

    /**
     * fuer Testzwecke
     */
    TimeSeriesCollection getDataset() {
        return dataset;
    }

    /**
     * nur fuer Testzwecke
     */
    void setChart(final JFreeChart mockedChart) {
        chart = mockedChart;
    }

    /**
     * nur fuer Testzwecke
     */
    XYBarRenderer getRenderer() {
        return renderer;
    }
}
