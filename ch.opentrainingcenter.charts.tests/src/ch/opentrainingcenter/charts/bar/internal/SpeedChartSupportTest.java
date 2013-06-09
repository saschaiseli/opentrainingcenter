package ch.opentrainingcenter.charts.bar.internal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.XYSeries;
import org.junit.Test;

public class SpeedChartSupportTest {

    @Test
    public void testPutPointsInSerie() {
        final Comparable<Integer> key = new Comparable<Integer>() {

            @Override
            public int compareTo(final Integer o) {
                return 0;
            }
        };
        final XYSeries serie = new XYSeries(key);
        final List<PositionPace> positionPaces = new ArrayList<>();

        final PositionPace pace = new PositionPace(1d, 2d);
        positionPaces.add(pace);
        final XYSeries result = SpeedChartSupport.putPointsInSerie(serie, positionPaces);
        assertEquals(1, result.getItemCount());
    }
}
