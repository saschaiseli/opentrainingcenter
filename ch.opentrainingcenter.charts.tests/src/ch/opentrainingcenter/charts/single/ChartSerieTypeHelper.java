package ch.opentrainingcenter.charts.single;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.opentrainingcenter.charts.single.XAxisChart;

public class ChartSerieTypeHelper {

    @Test
    public void testEnum() {
        final XAxisChart jahr = XAxisChart.YEAR;
        final XAxisChart month = XAxisChart.MONTH;
        final XAxisChart woche = XAxisChart.WEEK;
        final XAxisChart tag = XAxisChart.DAY;

        assertNotNull(jahr.getName());
        assertNotNull(month.getName());
        assertNotNull(woche.getName());
        assertNotNull(tag.getName());

        assertNotNull(jahr.getLabel());
        assertNotNull(month.getLabel());
        assertNotNull(woche.getLabel());
        assertNull(tag.getLabel());

        assertNotNull(jahr.getFormatPattern());
        assertNotNull(month.getFormatPattern());
        assertNotNull(woche.getFormatPattern());
        assertNull(tag.getFormatPattern());

        assertTrue(jahr.isLabelVisible());
        assertTrue(month.isLabelVisible());
        assertTrue(woche.isLabelVisible());
        assertFalse(tag.isLabelVisible());
    }

}
