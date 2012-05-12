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
        assertEquals(Messages.ChartType_0, ChartType.HEART_DISTANCE.getTitel());
        assertEquals(Messages.ChartType_3, ChartType.SPEED_DISTANCE.getTitel());
        assertEquals(Messages.ChartType_6, ChartType.ALTITUDE_DISTANCE.getTitel());
    }

    @Test
    public void testAllChartTypesYAchse() {
        assertEquals(Messages.ChartType_1, ChartType.HEART_DISTANCE.getyAchse());
        assertEquals(Messages.ChartType_4, ChartType.SPEED_DISTANCE.getyAchse());
        assertEquals(Messages.ChartType_7, ChartType.ALTITUDE_DISTANCE.getyAchse());
    }

    @Test
    public void testAllChartTypesXAchse() {
        assertEquals(Messages.ChartType_2, ChartType.HEART_DISTANCE.getxAchse());
        assertEquals(Messages.ChartType_5, ChartType.SPEED_DISTANCE.getxAchse());
        assertEquals(Messages.ChartType_8, ChartType.ALTITUDE_DISTANCE.getxAchse());
    }
}
