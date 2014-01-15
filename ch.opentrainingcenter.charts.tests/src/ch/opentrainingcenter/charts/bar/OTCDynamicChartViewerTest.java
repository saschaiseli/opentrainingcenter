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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.charts.bar.internal.OTCBarPainter;
import ch.opentrainingcenter.charts.ng.SimpleTrainingChart;
import ch.opentrainingcenter.charts.single.ChartSerieType;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class OTCDynamicChartViewerTest {

    private OTCDynamicChartViewer viewer;
    private IPreferenceStore store;

    @Before
    public void setup() {
        store = mock(IPreferenceStore.class);
        viewer = new OTCDynamicChartViewer(store);
    }

    @Test
    public void testGetSeriesType() {
        assertTrue(Day.class.equals(viewer.getSeriesType(ChartSerieType.DAY)));
        assertTrue(Week.class.equals(viewer.getSeriesType(ChartSerieType.WEEK)));
        assertTrue(Month.class.equals(viewer.getSeriesType(ChartSerieType.MONTH)));
        assertTrue(Year.class.equals(viewer.getSeriesType(ChartSerieType.YEAR)));
    }

    @Test
    public void testUpdateData_Herz() {
        final List<ISimpleTraining> data = new ArrayList<>();
        final ISimpleTraining simpleTraining = mock(ISimpleTraining.class);
        final Integer heart = Integer.valueOf(170);
        when(simpleTraining.getAvgHeartRate()).thenReturn(heart);
        final Date date = new Date(1000);
        when(simpleTraining.getDatum()).thenReturn(date);
        data.add(simpleTraining);

        viewer.updateData(data, ChartSerieType.DAY, SimpleTrainingChart.HERZ);

        final TimeSeries serie = viewer.getSerie();
        final TimeSeriesDataItem dataItem = (TimeSeriesDataItem) serie.getItems().get(0);
        assertEquals(1, serie.getItems().size());
        assertEquals((double) heart, dataItem.getValue());
        final RegularTimePeriod period = RegularTimePeriod.createInstance(Day.class, date, Calendar.getInstance().getTimeZone());
        assertEquals(period, dataItem.getPeriod());
    }

    @Test
    public void testInitData_Distanz() {
        final List<ISimpleTraining> data = new ArrayList<>();
        final ISimpleTraining simpleTraining = mock(ISimpleTraining.class);
        final Double distanz = Double.valueOf(42000);
        when(simpleTraining.getDistanzInMeter()).thenReturn(distanz);
        final Date date = new Date(1000);
        when(simpleTraining.getDatum()).thenReturn(date);
        data.add(simpleTraining);

        viewer.init(data, ChartSerieType.DAY);

        final TimeSeriesCollection dataset = viewer.getDataset();
        final TimeSeries serie = (TimeSeries) dataset.getSeries().get(0);
        final TimeSeriesDataItem dataItem = (TimeSeriesDataItem) serie.getItems().get(0);
        assertEquals(1, serie.getItems().size());
        assertEquals(distanz / 1000, dataItem.getValue());
        final RegularTimePeriod period = RegularTimePeriod.createInstance(Day.class, date, Calendar.getInstance().getTimeZone());
        assertEquals(period, dataItem.getPeriod());

    }

    @Test
    public void testUpdateData_Distanz() {
        final List<ISimpleTraining> data = new ArrayList<>();
        final ISimpleTraining simpleTraining = mock(ISimpleTraining.class);
        final Double distanz = Double.valueOf(42000);
        when(simpleTraining.getDistanzInMeter()).thenReturn(distanz);
        final Date date = new Date(1000);
        when(simpleTraining.getDatum()).thenReturn(date);
        data.add(simpleTraining);

        viewer.updateData(data, ChartSerieType.DAY, SimpleTrainingChart.DISTANZ);

        final TimeSeries serie = viewer.getSerie();
        final TimeSeriesDataItem dataItem = (TimeSeriesDataItem) serie.getItems().get(0);
        assertEquals(1, serie.getItems().size());
        assertEquals(distanz / 1000, dataItem.getValue());
        final RegularTimePeriod period = RegularTimePeriod.createInstance(Day.class, date, Calendar.getInstance().getTimeZone());
        assertEquals(period, dataItem.getPeriod());
    }

    @Test
    public void updateAxisChart_NotCreated() {
        viewer.updateAxis(SimpleTrainingChart.DISTANZ);
    }

    @Test
    public void updateAxisChartAlreadyCreated_Distanz() {
        final JFreeChart mockedChart = mock(JFreeChart.class);
        final XYPlot plot = mock(XYPlot.class);
        final ValueAxis rangeAxis = mock(ValueAxis.class);
        when(plot.getRangeAxis()).thenReturn(rangeAxis);
        when(mockedChart.getXYPlot()).thenReturn(plot);
        viewer.setChart(mockedChart);

        viewer.updateAxis(SimpleTrainingChart.DISTANZ);

        verify(rangeAxis).setLabel(SimpleTrainingChart.DISTANZ.getyAchse());
        verify(mockedChart).setTitle(SimpleTrainingChart.DISTANZ.getTitle());
    }

    @Test
    public void updateRendererDay() {
        final Color color = new Color(1, 2, 3, OTCDynamicChartViewer.ALPHA);
        when(store.getString(PreferenceConstants.DISTANCE_CHART_COLOR)).thenReturn("1,2,3");

        // Execute
        viewer.updateRenderer(ChartSerieType.DAY, false);

        final XYBarRenderer renderer = viewer.getRenderer();

        assertNotNull(renderer);
        final Color result = (Color) renderer.getSeriesPaint(0);
        assertEquals(color, result);

        final XYBarPainter barPainter = renderer.getBarPainter();

        assertTrue(barPainter instanceof OTCBarPainter);

        assertEquals(0.1, renderer.getMargin(), 0.001);
    }

    @Test
    public void updateRendererMonth() {
        final Color color = new Color(1, 2, 3, OTCDynamicChartViewer.ALPHA);
        when(store.getString(PreferenceConstants.DISTANCE_CHART_COLOR)).thenReturn("1,2,3");

        final JFreeChart mockedChart = mock(JFreeChart.class);
        final XYPlot plot = mock(XYPlot.class);
        when(mockedChart.getXYPlot()).thenReturn(plot);
        viewer.setChart(mockedChart);

        // Execute
        viewer.updateRenderer(ChartSerieType.MONTH, false);

        final XYBarRenderer renderer = viewer.getRenderer();

        assertNotNull(renderer);
        final Color result = (Color) renderer.getSeriesPaint(0);
        assertEquals(color, result);

        final XYBarPainter barPainter = renderer.getBarPainter();

        assertTrue(barPainter instanceof OTCBarPainter);

        assertEquals(0.1, renderer.getMargin(), 0.001);

        verify(plot).setRenderer(renderer);
    }

    @Test
    public void testCreateChart() {

        final JFreeChart chart = viewer.createChart(ChartSerieType.MONTH, SimpleTrainingChart.DISTANZ);

        assertNotNull(chart);

        assertEquals(SimpleTrainingChart.DISTANZ.getTitle(), chart.getTitle().getText());
        assertEquals(SimpleTrainingChart.DISTANZ.getyAchse(), chart.getXYPlot().getRangeAxis().getLabel());
        assertEquals(SimpleTrainingChart.DISTANZ.getxAchse(), chart.getXYPlot().getDomainAxis().getLabel());
    }
}
