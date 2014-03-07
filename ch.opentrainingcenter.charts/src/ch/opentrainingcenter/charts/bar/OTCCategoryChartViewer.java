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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.TextAnchor;

import ch.opentrainingcenter.charts.bar.internal.OTCBarPainter;
import ch.opentrainingcenter.charts.bar.internal.ValueMapper;
import ch.opentrainingcenter.charts.ng.SimpleTrainingChart;
import ch.opentrainingcenter.charts.single.ChartSerieType;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.model.training.ISimpleTraining;

public class OTCCategoryChartViewer {

    private static final String DECIMAL = "0.000"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(OTCDynamicChartViewer.class);

    static final int ALPHA = 255;

    private ChartComposite chartComposite;

    private final TimeSeries serie = new TimeSeries("TimeChart"); //$NON-NLS-1$

    private JFreeChart chart;

    private final IPreferenceStore store;

    private Composite parent;

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public OTCCategoryChartViewer(final IPreferenceStore store) {
        this.store = store;
    }

    public void setParent(final Composite parent) {
        this.parent = parent;
    }

    public void createPartControl(final ChartSerieType chartSerieType, final SimpleTrainingChart chartType, final List<ISimpleTraining> dataNow,
            final List<ISimpleTraining> dataPast) {

        init(dataNow, dataPast, chartSerieType);

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
        chart = ChartFactory.createBarChart(chartType.getTitle(), chartType.getxAchse(), chartType.getyAchse(), dataset, PlotOrientation.VERTICAL, true, true,
                false);
        chart.setAntiAlias(true);
        chart.setBorderVisible(false);
        chart.setAntiAlias(true);
        chart.setBorderVisible(true);
        chart.setTextAntiAlias(true);
        chart.setBackgroundPaint(Color.white);
        chart.setBorderPaint(Color.white);

        updateRenderer(type, false, chartType);
        //
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setRangeCrosshairLockedOnData(true);
        return chart;
    }

    /**
     * Aktualisiert die Daten der Timeserie
     */
    public void updateData(final List<ISimpleTraining> dataPast, final List<ISimpleTraining> dataNow, final ChartSerieType type,
            final SimpleTrainingChart chartType) {

        updateAxis(chartType);
        dataset.clear();
        if (!ChartSerieType.DAY.equals(type)) {
            updateDataSet(dataPast, type, chartType, "dataPast");
        }
        updateDataSet(dataNow, type, chartType, "dataNow");
    }

    private void updateDataSet(final List<ISimpleTraining> data, final ChartSerieType type, final SimpleTrainingChart stc, final String rowName) {
        final List<ValueMapper> values = convertAndSort(data, stc, type);
        for (final ValueMapper t : values) {
            dataset.addValue(t.getValue(stc), rowName, t.getCategory());
        }
    }

    private List<ValueMapper> convertAndSort(final List<ISimpleTraining> data, final SimpleTrainingChart stc, final ChartSerieType type) {
        final List<ValueMapper> result = new ArrayList<>();
        for (final ISimpleTraining training : data) {
            result.add(new ValueMapper(training, type));
        }
        Collections.sort(result);
        return result;
    }

    void updateAxis(final SimpleTrainingChart chartType) {
        if (chart != null) {
            // update axis & title
            chart.setTitle(chartType.getTitle());
            ((CategoryPlot) chart.getPlot()).getRangeAxis().setLabel(chartType.getyAchse());
        }
    }

    void init(final List<ISimpleTraining> dataNow, final List<ISimpleTraining> dataPast, final ChartSerieType type) {
        dataset.clear();
        updateData(dataPast, dataNow, type, SimpleTrainingChart.DISTANZ);
    }

    public void updateRenderer(final ChartSerieType type, final boolean compare, final SimpleTrainingChart chartType) {
        final BarRenderer renderer = new BarRenderer();
        setSeriesPaint(renderer, chartType);

        final OTCBarPainter painter = new OTCBarPainter();
        renderer.setBarPainter(painter);
        renderer.setItemMargin(0.0);

        if (type.isLabelVisible()) {
            final String label = "{2}km (" + type.getLabel() + "{1})"; //$NON-NLS-1$//$NON-NLS-2$
            final SimpleDateFormat datePattern = new SimpleDateFormat(type.getFormatPattern());
            final StandardXYToolTipGenerator generator = new StandardXYToolTipGenerator(label, datePattern, new DecimalFormat(DECIMAL));
            // renderer.setBaseToolTipGenerator(generator);
            //            renderer.setBaseItemLabelFont(new Font("Default", Font.PLAIN, 7)); //$NON-NLS-1$
            renderer.setBaseItemLabelGenerator(null);

            final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, -Math.PI / 2.0);
            renderer.setBasePositiveItemLabelPosition(position);

            final XYItemLabelGenerator lgen = new StandardXYItemLabelGenerator(label, datePattern, new DecimalFormat(DECIMAL));
            // renderer.setBaseItemLabelGenerator(lgen);
            renderer.setBaseItemLabelsVisible(true);
        }
        if (chart != null) {
            chart.getCategoryPlot().setRenderer(renderer);
        }
    }

    private void setSeriesPaint(final AbstractRenderer renderer, final SimpleTrainingChart chartType) {
        if (SimpleTrainingChart.DISTANZ.equals(chartType)) {
            renderer.setSeriesPaint(0, ColorFromPreferenceHelper.getColor(store, PreferenceConstants.CHART_DISTANCE_COLOR_PAST, ALPHA));
            renderer.setSeriesPaint(1, ColorFromPreferenceHelper.getColor(store, PreferenceConstants.CHART_DISTANCE_COLOR, ALPHA));
        } else {
            renderer.setSeriesPaint(0, ColorFromPreferenceHelper.getColor(store, PreferenceConstants.CHART_HEART_COLOR_PAST, ALPHA));
            renderer.setSeriesPaint(1, ColorFromPreferenceHelper.getColor(store, PreferenceConstants.CHART_HEART_COLOR, ALPHA));
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

    // /**
    // * fuer Testzwecke
    // */
    // TimeSeriesCollection getDataset() {
    // return dataset;
    // }

    /**
     * nur fuer Testzwecke
     */
    void setChart(final JFreeChart mockedChart) {
        chart = mockedChart;
    }
}
