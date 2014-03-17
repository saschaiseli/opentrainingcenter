package ch.opentrainingcenter.charts.single;

import org.junit.Test;

import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.i18n.Messages;
import static org.junit.Assert.assertEquals;

public class XAxisChartTest {
    @Test
    public void testName() {
        assertEquals(Messages.ChartSerieType0, XAxisChart.YEAR.getName());
        assertEquals(Messages.ChartSerieType1, XAxisChart.MONTH.getName());
        assertEquals(Messages.ChartSerieType4, XAxisChart.WEEK.getName());
        assertEquals(Messages.ChartSerieType7, XAxisChart.DAY.getName());
    }

    @Test
    public void testFormatPattern() {
        assertEquals(Messages.ChartSerieType8, XAxisChart.YEAR.getFormatPattern());
        assertEquals(Messages.ChartSerieType2, XAxisChart.MONTH.getFormatPattern());
        assertEquals(Messages.ChartSerieType5, XAxisChart.WEEK.getFormatPattern());
        assertEquals(null, XAxisChart.DAY.getFormatPattern());
    }

    @Test
    public void testLabel() {
        assertEquals(Messages.ChartSerieType3, XAxisChart.YEAR.getLabel());
        assertEquals(Messages.ChartSerieType3, XAxisChart.MONTH.getLabel());
        assertEquals(Messages.ChartSerieType6, XAxisChart.WEEK.getLabel());
        assertEquals(null, XAxisChart.DAY.getLabel());
    }

    @Test
    public void testLabelVisible() {
        assertEquals(true, XAxisChart.YEAR.isLabelVisible());
        assertEquals(true, XAxisChart.MONTH.isLabelVisible());
        assertEquals(true, XAxisChart.WEEK.isLabelVisible());
        assertEquals(false, XAxisChart.DAY.isLabelVisible());
    }
}
