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
import java.text.DecimalFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.TextAnchor;

import ch.opentrainingcenter.charts.bar.internal.CategoryHelper;
import ch.opentrainingcenter.charts.bar.internal.ChartDataSupport;
import ch.opentrainingcenter.charts.bar.internal.ChartDataWrapper;
import ch.opentrainingcenter.charts.bar.internal.OTCBarPainter;
import ch.opentrainingcenter.charts.ng.TrainingChart;
import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.charts.PastTraining;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.transfer.ITraining;

public class OTCCategoryChartViewer {

    private static final String DECIMAL = "0.000"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(OTCCategoryChartViewer.class);

    static final int ALPHA = 255;

    private ChartComposite chartComposite;

    private JFreeChart chart;

    private final IPreferenceStore store;

    private Composite parent;

    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    private BarRenderer renderer;

    public OTCCategoryChartViewer(final IPreferenceStore store) {
        this.store = store;
    }

    public void setParent(final Composite parent) {
        this.parent = parent;
    }

    public void createPartControl() {

        createChart();
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

    JFreeChart createChart() {
        chart = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.VERTICAL, false, true, false); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        chart.setAntiAlias(true);
        chart.setBorderVisible(false);
        chart.setAntiAlias(true);
        chart.setBorderVisible(true);
        chart.setTextAntiAlias(true);
        chart.setBackgroundPaint(Color.white);
        chart.setBorderPaint(Color.white);

        //
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        return chart;
    }

    /**
     * Aktualisiert die Daten
     * 
     * @param dataNow
     *            Daten von diesem jahr
     * @param dataPast
     *            Vergangene Daten
     * @param chartSerieType
     * @param chartType
     * @param compareLast
     *            mit letztem Jahr vergleichen
     */
    public void updateData(final List<ITraining> dataNow, final List<PastTraining> dataPast, final XAxisChart xAxis, final TrainingChart chartType,
            final boolean compareLast) {

        updateAxis(chartType, xAxis);

        dataset.clear();

        final ChartDataSupport support = new ChartDataSupport(xAxis);
        final List<ChartDataWrapper> now = support.convertAndSort(dataNow);

        int i = 0;
        if (isComparable(xAxis, compareLast)) {
            for (final PastTraining training : dataPast) {
                final List<ChartDataWrapper> past = support.createPastData(training, now);
                addValues(past, String.valueOf(i), chartType, xAxis);
                i++;
            }
        }
        addValues(now, String.valueOf(i), chartType, xAxis);
    }

    private void addValues(final List<ChartDataWrapper> chartDatas, final String rowName, final TrainingChart stc, final XAxisChart xAxis) {
        for (final ChartDataWrapper chartData : chartDatas) {
            dataset.addValue(chartData.getValue(stc), rowName, chartData.getCategory());
        }
    }

    void updateAxis(final TrainingChart chartType, final XAxisChart chartSerieType) {
        if (chart != null) {
            chart.setTitle(chartType.getTitle());
            ((CategoryPlot) chart.getPlot()).getRangeAxis().setLabel(chartType.getyAchse());
            final CategoryAxis domainAxis = ((CategoryPlot) chart.getPlot()).getDomainAxis();
            domainAxis.setLabel(CategoryHelper.getDomainAxis(chartSerieType));
            if (XAxisChart.YEAR.equals(chartSerieType)) {
                domainAxis.setCategoryMargin(-0.75d);
            } else {
                domainAxis.setCategoryMargin(0.2d);
            }
        }
    }

    public void updateRenderer(final XAxisChart type, final TrainingChart chartType, final boolean compareLast) {
        renderer = new BarRenderer();
        setSeriesPaint(renderer, chartType, type, compareLast);

        final OTCBarPainter painter = new OTCBarPainter();
        renderer.setBarPainter(painter);
        renderer.setItemMargin(0.0);

        final String labelPattern = "{2} "; //$NON-NLS-1$
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(labelPattern, new DecimalFormat(DECIMAL)));

        final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, -Math.PI / 2.0);
        renderer.setBasePositiveItemLabelPosition(position);

        final CategoryItemLabelGenerator cl = new StandardCategoryItemLabelGenerator(labelPattern, new DecimalFormat(DECIMAL));
        renderer.setBaseItemLabelGenerator(cl);
        renderer.setBaseItemLabelsVisible(true);
        if (chart != null) {
            chart.getCategoryPlot().setRenderer(renderer);
        }
    }

    private void setSeriesPaint(final AbstractRenderer renderer, final TrainingChart chartType, final XAxisChart type, final boolean compareLast) {
        final String color;
        if (TrainingChart.DISTANZ.equals(chartType)) {
            color = PreferenceConstants.CHART_DISTANCE_COLOR;
        } else {
            color = PreferenceConstants.CHART_HEART_COLOR;
        }
        final Color barColor = ColorFromPreferenceHelper.getColor(store, color, ALPHA);
        renderer.setSeriesPaint(0, barColor.brighter().brighter());
        renderer.setSeriesPaint(1, barColor.brighter());
        renderer.setSeriesPaint(2, barColor);
    }

    private boolean isComparable(final XAxisChart type, final boolean compareLast) {
        return compareLast && !XAxisChart.DAY.equals(type);
    }

    Class<? extends RegularTimePeriod> getSeriesType(final XAxisChart chartType) {
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
     * nur fuer Testzwecke
     */
    void setChart(final JFreeChart mockedChart) {
        chart = mockedChart;
    }

    /**
     * nur fuer Testzwecke
     */
    DefaultCategoryDataset getDataset() {
        return dataset;
    }

    public BarRenderer getRenderer() {
        return renderer;
    }

}
