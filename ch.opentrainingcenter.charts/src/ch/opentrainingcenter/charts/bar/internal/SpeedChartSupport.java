package ch.opentrainingcenter.charts.bar.internal;

import java.util.List;

import org.jfree.data.xy.XYSeries;

public class SpeedChartSupport {

    public static XYSeries putPointsInSerie(final XYSeries serie, final List<PositionPace> positionPaces) {
        for (final PositionPace positionPace : positionPaces) {
            serie.add(positionPace.getPosition(), positionPace.getPace());
        }
        return serie;
    }
}
