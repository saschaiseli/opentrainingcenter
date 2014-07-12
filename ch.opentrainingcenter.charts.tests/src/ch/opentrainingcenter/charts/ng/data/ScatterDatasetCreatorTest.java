package ch.opentrainingcenter.charts.ng.data;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jfree.data.xy.XYDataset;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class ScatterDatasetCreatorTest {

    private Map<String, Map<Integer, Integer>> all;

    @Before
    public void setUp() {
        all = new HashMap<>();
    }

    @Test
    public void testPopulateOneSerie() {
        final Map<Integer, Integer> value = new HashMap<>();
        value.put(185, 42);
        all.put("test", value);

        final XYDataset result = ScatterDatasetCreator.createDataset(all);

        assertEquals(1, result.getSeriesCount());
        assertEquals(185, result.getXValue(0, 0), 0001);
        assertEquals(42, result.getYValue(0, 0), 0001);
    }

    @Test
    public void testPopulateTwoSerie() {
        final Map<Integer, Integer> valueA = new HashMap<>();
        valueA.put(185, 42);

        final Map<Integer, Integer> valueB = new HashMap<>();
        valueB.put(185, 42);
        valueB.put(186, 142);

        all.put("testA", valueA);
        all.put("testB", valueB);

        final XYDataset result = ScatterDatasetCreator.createDataset(all);

        assertEquals(2, result.getSeriesCount());
        assertEquals(185, result.getXValue(0, 0), 0001);
        assertEquals(42, result.getYValue(0, 0), 0001);

        assertEquals(185, result.getXValue(1, 0), 0001);
        assertEquals(42, result.getYValue(1, 0), 0001);

        assertEquals(186, result.getXValue(0, 1), 0001);
        assertEquals(142, result.getYValue(0, 1), 0001);
    }
}
