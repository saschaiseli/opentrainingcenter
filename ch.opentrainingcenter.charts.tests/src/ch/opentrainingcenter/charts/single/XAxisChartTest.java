package ch.opentrainingcenter.charts.single;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;

public class XAxisChartTest {
    @Test
    public void testName() {
        assertEquals(Messages.ChartSerieType0, XAxisChart.YEAR.getName());
        assertEquals(Messages.ChartSerieType1, XAxisChart.MONTH.getName());
        assertEquals(Messages.ChartSerieType4, XAxisChart.WEEK.getName());
        assertEquals(Messages.ChartSerieType7, XAxisChart.DAY.getName());
        assertEquals(Messages.ChartSerieType9, XAxisChart.YEAR_START_TILL_NOW.getName());
    }

    @Test
    public void testFormatPattern() {
        assertEquals(Messages.ChartSerieType8, XAxisChart.YEAR.getFormatPattern());
        assertEquals(Messages.ChartSerieType8, XAxisChart.YEAR_START_TILL_NOW.getFormatPattern());
        assertEquals(Messages.ChartSerieType2, XAxisChart.MONTH.getFormatPattern());
        assertEquals(Messages.ChartSerieType5, XAxisChart.WEEK.getFormatPattern());
        assertEquals(null, XAxisChart.DAY.getFormatPattern());
    }

    @Test
    public void testLabel() {
        assertEquals(Messages.ChartSerieType3, XAxisChart.YEAR.getLabel());
        assertEquals(Messages.ChartSerieType3, XAxisChart.YEAR_START_TILL_NOW.getLabel());
        assertEquals(Messages.ChartSerieType3, XAxisChart.MONTH.getLabel());
        assertEquals(Messages.ChartSerieType6, XAxisChart.WEEK.getLabel());
        assertEquals(null, XAxisChart.DAY.getLabel());
    }

    @Test
    public void testLabelVisible() {
        assertEquals(true, XAxisChart.YEAR.isLabelVisible());
        assertEquals(true, XAxisChart.YEAR_START_TILL_NOW.isLabelVisible());
        assertEquals(true, XAxisChart.MONTH.isLabelVisible());
        assertEquals(true, XAxisChart.WEEK.isLabelVisible());
        assertEquals(false, XAxisChart.DAY.isLabelVisible());
    }

    @Test
    public void testIndex() {
        assertEquals(XAxisChart.DAY, XAxisChart.getByIndex(0));
        assertEquals(XAxisChart.WEEK, XAxisChart.getByIndex(1));
        assertEquals(XAxisChart.MONTH, XAxisChart.getByIndex(2));
        assertEquals(XAxisChart.YEAR_START_TILL_NOW, XAxisChart.getByIndex(3));
        assertEquals(XAxisChart.YEAR, XAxisChart.getByIndex(4));
    }
}
