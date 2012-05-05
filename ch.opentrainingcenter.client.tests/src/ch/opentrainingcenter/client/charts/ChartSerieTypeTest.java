package ch.opentrainingcenter.client.charts;

import org.junit.Test;

import ch.opentrainingcenter.client.Messages;
import static org.junit.Assert.assertEquals;

public class ChartSerieTypeTest {
    @Test
    public void testName() {
        assertEquals(Messages.ChartSerieType_0, ChartSerieType.YEAR.getName());
        assertEquals(Messages.ChartSerieType_1, ChartSerieType.MONTH.getName());
        assertEquals(Messages.ChartSerieType_4, ChartSerieType.WEEK.getName());
        assertEquals(Messages.ChartSerieType_7, ChartSerieType.DAY.getName());
    }

    @Test
    public void testFormatPattern() {
        assertEquals(Messages.ChartSerieType_8, ChartSerieType.YEAR.getFormatPattern());
        assertEquals(Messages.ChartSerieType_2, ChartSerieType.MONTH.getFormatPattern());
        assertEquals(Messages.ChartSerieType_5, ChartSerieType.WEEK.getFormatPattern());
        assertEquals(null, ChartSerieType.DAY.getFormatPattern());
    }

    @Test
    public void testLabel() {
        assertEquals(Messages.ChartSerieType_3, ChartSerieType.YEAR.getLabel());
        assertEquals(Messages.ChartSerieType_3, ChartSerieType.MONTH.getLabel());
        assertEquals(Messages.ChartSerieType_6, ChartSerieType.WEEK.getLabel());
        assertEquals(null, ChartSerieType.DAY.getLabel());
    }

    @Test
    public void testLabelVisible() {
        assertEquals(true, ChartSerieType.YEAR.isLabelVisible());
        assertEquals(true, ChartSerieType.MONTH.isLabelVisible());
        assertEquals(true, ChartSerieType.WEEK.isLabelVisible());
        assertEquals(false, ChartSerieType.DAY.isLabelVisible());
    }
}
