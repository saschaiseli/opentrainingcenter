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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarPainter;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.charts.bar.internal.CategoryHelper;
import ch.opentrainingcenter.charts.bar.internal.OTCBarPainter;
import ch.opentrainingcenter.charts.ng.TrainingChart;
import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class OTCCategoryChartViewerTest {

    private OTCCategoryChartViewer viewer;
    private IPreferenceStore store;

    @Before
    public void setup() {
        store = mock(IPreferenceStore.class);
        viewer = new OTCCategoryChartViewer(store);
    }

    @Test
    public void testGetSeriesType() {
        assertTrue(Day.class.equals(viewer.getSeriesType(XAxisChart.DAY)));
        assertTrue(Week.class.equals(viewer.getSeriesType(XAxisChart.WEEK)));
        assertTrue(Month.class.equals(viewer.getSeriesType(XAxisChart.MONTH)));
        assertTrue(Year.class.equals(viewer.getSeriesType(XAxisChart.YEAR)));
    }

    @Test
    public void testUpdateData_Herz() {
        final List<ITraining> data = new ArrayList<>();
        final ITraining simpleTraining = mock(ITraining.class);
        final Integer heart = Integer.valueOf(170);
        when(simpleTraining.getAverageHeartBeat()).thenReturn(heart);
        when(simpleTraining.getDatum()).thenReturn(1000L);
        data.add(simpleTraining);

        viewer.updateData(data, data, XAxisChart.DAY, TrainingChart.HERZ, false);

        final DefaultCategoryDataset dataset = viewer.getDataset();
        final Number value = dataset.getValue(OTCCategoryChartViewer.DIESES_JAHR, "1");

        assertEquals(170, value.intValue());
    }

    @Test
    public void testInitData_Distanz() {
        final List<ITraining> data = new ArrayList<>();
        final ITraining simpleTraining = mock(ITraining.class);
        final Double distanz = Double.valueOf(42000);
        when(simpleTraining.getLaengeInMeter()).thenReturn(distanz);
        when(simpleTraining.getDatum()).thenReturn(1000L);
        data.add(simpleTraining);

        viewer.createChart();
        viewer.updateData(data, data, XAxisChart.DAY, TrainingChart.DISTANZ, false);

        final DefaultCategoryDataset dataset = viewer.getDataset();
        final Number value = dataset.getValue(OTCCategoryChartViewer.DIESES_JAHR, "1");

        assertEquals((int) (distanz / 1000), value.intValue());
    }

    @Test
    public void testUpdateData_Distanz() {
        final List<ITraining> data = new ArrayList<>();
        final ITraining simpleTraining = mock(ITraining.class);
        final Double distanz = Double.valueOf(42000);
        when(simpleTraining.getLaengeInMeter()).thenReturn(distanz);
        when(simpleTraining.getDatum()).thenReturn(1000L);
        data.add(simpleTraining);

        viewer.updateData(data, data, XAxisChart.DAY, TrainingChart.DISTANZ, false);

        final DefaultCategoryDataset dataset = viewer.getDataset();
        final Number value = dataset.getValue(OTCCategoryChartViewer.DIESES_JAHR, "1");

        assertEquals((int) (distanz / 1000), value.intValue());
    }

    @Test
    public void updateAxisChart_NotCreated() {
        viewer.updateAxis(TrainingChart.DISTANZ, XAxisChart.DAY);
    }

    @Test
    public void updateAxisChartAlreadyCreated_Distanz() {
        final JFreeChart mockedChart = mock(JFreeChart.class);
        final CategoryPlot plot = mock(CategoryPlot.class);
        final ValueAxis rangeAxis = mock(ValueAxis.class);
        when(plot.getRangeAxis()).thenReturn(rangeAxis);
        final CategoryAxis domainAxis = mock(CategoryAxis.class);
        when(plot.getDomainAxis()).thenReturn(domainAxis);
        when(mockedChart.getPlot()).thenReturn(plot);
        viewer.setChart(mockedChart);

        viewer.updateAxis(TrainingChart.DISTANZ, XAxisChart.DAY);

        verify(rangeAxis).setLabel(TrainingChart.DISTANZ.getyAchse());
        verify(domainAxis).setLabel(CategoryHelper.getDomainAxis(XAxisChart.DAY));
        verify(mockedChart).setTitle(TrainingChart.DISTANZ.getTitle());
    }

    @Test
    public void updateRendererDay_Kein_Past_Renderer() {
        final Color colorNow = new Color(1, 2, 3, OTCCategoryChartViewer.ALPHA);
        when(store.getString(PreferenceConstants.CHART_DISTANCE_COLOR)).thenReturn("1,2,3");
        when(store.getString(PreferenceConstants.CHART_DISTANCE_COLOR_PAST)).thenReturn("2,3,4");

        // Execute
        viewer.updateRenderer(XAxisChart.DAY, TrainingChart.DISTANZ, true);

        final BarRenderer renderer = viewer.getRenderer();

        assertNotNull(renderer);

        final Color past = (Color) renderer.getSeriesPaint(0);
        assertEquals(colorNow, past);

        final Color now = (Color) renderer.getSeriesPaint(1);
        assertEquals(colorNow, now);

        final BarPainter barPainter = renderer.getBarPainter();

        assertTrue(barPainter instanceof OTCBarPainter);

        assertEquals(0.0, renderer.getItemMargin(), 0.001);
    }

    @Test
    public void updateRendererMonth() {
        final Color colorNow = new Color(1, 2, 3, OTCCategoryChartViewer.ALPHA);
        final Color colorPast = new Color(2, 3, 4, OTCCategoryChartViewer.ALPHA);
        when(store.getString(PreferenceConstants.CHART_DISTANCE_COLOR)).thenReturn("1,2,3");
        when(store.getString(PreferenceConstants.CHART_DISTANCE_COLOR_PAST)).thenReturn("2,3,4");

        final JFreeChart mockedChart = mock(JFreeChart.class);
        final CategoryPlot plot = mock(CategoryPlot.class);
        when(mockedChart.getCategoryPlot()).thenReturn(plot);
        viewer.setChart(mockedChart);

        // Execute
        viewer.updateRenderer(XAxisChart.MONTH, TrainingChart.DISTANZ, true);

        final BarRenderer renderer = viewer.getRenderer();

        assertNotNull(renderer);
        final Color past = (Color) renderer.getSeriesPaint(0);
        assertEquals(colorPast, past);

        final Color now = (Color) renderer.getSeriesPaint(1);
        assertEquals(colorNow, now);

        final BarPainter barPainter = renderer.getBarPainter();

        assertTrue(barPainter instanceof OTCBarPainter);

        assertEquals(0.0, renderer.getItemMargin(), 0.001);

        verify(plot).setRenderer(renderer);
    }

    @Test
    public void testCreateChart() {

        final JFreeChart chart = viewer.createChart();

        viewer.updateData(new ArrayList<ITraining>(), new ArrayList<ITraining>(), XAxisChart.MONTH, TrainingChart.DISTANZ, false);

        assertNotNull(chart);

        assertEquals(TrainingChart.DISTANZ.getTitle(), chart.getTitle().getText());
        assertEquals(TrainingChart.DISTANZ.getyAchse(), chart.getCategoryPlot().getRangeAxis().getLabel());
        assertEquals(CategoryHelper.getDomainAxis(XAxisChart.MONTH), chart.getCategoryPlot().getDomainAxis().getLabel());
    }
}
