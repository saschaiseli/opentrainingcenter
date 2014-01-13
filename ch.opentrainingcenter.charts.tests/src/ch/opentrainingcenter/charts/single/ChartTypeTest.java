package ch.opentrainingcenter.charts.single;

import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;
import static org.junit.Assert.assertEquals;

public class ChartTypeTest {

    @Test
    public void testGetTitel() {
        assertEquals(Messages.ChartType0, ChartType.HEART_DISTANCE.getTitel());
        assertEquals(Messages.ChartType3, ChartType.SPEED_DISTANCE.getTitel());
        assertEquals(Messages.ChartType6, ChartType.ALTITUDE_DISTANCE.getTitel());
    }

    @Test
    public void testGetxAchse() {
        assertEquals(Messages.ChartType2, ChartType.HEART_DISTANCE.getxAchse());
        assertEquals(Messages.ChartType5, ChartType.SPEED_DISTANCE.getxAchse());
        assertEquals(Messages.ChartType8, ChartType.ALTITUDE_DISTANCE.getxAchse());
    }

    @Test
    public void testGetyAchse() {
        assertEquals(Messages.ChartType1, ChartType.HEART_DISTANCE.getyAchse());
        assertEquals(Messages.ChartType4, ChartType.SPEED_DISTANCE.getyAchse());
        assertEquals(Messages.ChartType7, ChartType.ALTITUDE_DISTANCE.getyAchse());
    }

}
