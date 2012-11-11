package ch.opentrainingcenter.client.charts;

import org.junit.Test;

import ch.opentrainingcenter.core.helper.ChartSerieType;
import ch.opentrainingcenter.i18n.Messages;
import static org.junit.Assert.assertEquals;

public class ChartSerieTypeTest {
    @Test
    public void testName() {
        assertEquals(Messages.ChartSerieType0, ChartSerieType.YEAR.getName());
        assertEquals(Messages.ChartSerieType1, ChartSerieType.MONTH.getName());
        assertEquals(Messages.ChartSerieType4, ChartSerieType.WEEK.getName());
        assertEquals(Messages.ChartSerieType7, ChartSerieType.DAY.getName());
    }

    @Test
    public void testFormatPattern() {
        assertEquals(Messages.ChartSerieType8, ChartSerieType.YEAR.getFormatPattern());
        assertEquals(Messages.ChartSerieType2, ChartSerieType.MONTH.getFormatPattern());
        assertEquals(Messages.ChartSerieType5, ChartSerieType.WEEK.getFormatPattern());
        assertEquals(null, ChartSerieType.DAY.getFormatPattern());
    }

    @Test
    public void testLabel() {
        assertEquals(Messages.ChartSerieType3, ChartSerieType.YEAR.getLabel());
        assertEquals(Messages.ChartSerieType3, ChartSerieType.MONTH.getLabel());
        assertEquals(Messages.ChartSerieType6, ChartSerieType.WEEK.getLabel());
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
