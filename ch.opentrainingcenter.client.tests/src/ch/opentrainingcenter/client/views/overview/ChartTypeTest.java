package ch.opentrainingcenter.client.views.overview;

import org.junit.Test;

import ch.opentrainingcenter.client.Messages;
import static org.junit.Assert.assertEquals;

public class ChartTypeTest {
    @Test
    public void testChartTypeEnums() {
        assertEquals(3, ChartType.values().length);
    }

    @Test
    public void testAllChartTypesTitles() {
        assertEquals(Messages.ChartType0, ChartType.HEART_DISTANCE.getTitel());
        assertEquals(Messages.ChartType3, ChartType.SPEED_DISTANCE.getTitel());
        assertEquals(Messages.ChartType6, ChartType.ALTITUDE_DISTANCE.getTitel());
    }

    @Test
    public void testAllChartTypesYAchse() {
        assertEquals(Messages.ChartType1, ChartType.HEART_DISTANCE.getyAchse());
        assertEquals(Messages.ChartType4, ChartType.SPEED_DISTANCE.getyAchse());
        assertEquals(Messages.ChartType7, ChartType.ALTITUDE_DISTANCE.getyAchse());
    }

    @Test
    public void testAllChartTypesXAchse() {
        assertEquals(Messages.ChartType2, ChartType.HEART_DISTANCE.getxAchse());
        assertEquals(Messages.ChartType5, ChartType.SPEED_DISTANCE.getxAchse());
        assertEquals(Messages.ChartType8, ChartType.ALTITUDE_DISTANCE.getxAchse());
    }
}
