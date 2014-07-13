package ch.opentrainingcenter.charts.ng.data;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
        valueB.put(186, 43);
        valueB.put(187, 44);

        final String keyA = "testA";
        final String keyB = "testB";
        all.put(keyA, valueA);
        all.put(keyB, valueB);

        final XYSeriesCollection result = (XYSeriesCollection) ScatterDatasetCreator.createDataset(all);
        assertEquals(2, result.getSeriesCount());

        final XYSeries serieA = result.getSeries(keyA);
        final XYDataItem dataItemA = serieA.getDataItem(0);
        assertEquals(185.0, dataItemA.getX());
        assertEquals(42.0, dataItemA.getY());

        final XYSeries serieB = result.getSeries(keyB);
        final XYDataItem dataItemB0 = serieB.getDataItem(0);

        assertEquals(186.0, dataItemB0.getX());
        assertEquals(43.0, dataItemB0.getY());

        final XYDataItem dataItemB1 = serieB.getDataItem(1);
        assertEquals(187.0, dataItemB1.getX());
        assertEquals(44.0, dataItemB1.getY());
    }
}
