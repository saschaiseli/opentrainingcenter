package ch.opentrainingcenter.charts.single;

import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.ui.TextAnchor;

import ch.opentrainingcenter.core.data.Pair;

public class AnnotationHelper {

    static void addCrossAnnotation(final XYPlot plot, final Pair<Number, Number> xy, final double xMax, final double yMax, final String zeit) {
        final double x = xy.getFirst().doubleValue();
        final double y = xy.getSecond().doubleValue();
        plot.addAnnotation(new XYLineAnnotation(x, 0, x, Integer.MAX_VALUE));
        plot.addAnnotation(new XYLineAnnotation(0, y, Integer.MAX_VALUE, y));
        final Range range = plot.getRangeAxis(0).getRange();
        final double lowerBound = range.getLowerBound();
        final double upperBound = range.getUpperBound();
        final double diff = upperBound - lowerBound;

        final XYTextAnnotation xValue = new XYTextAnnotation("Meter: " + xy.getFirst(), x * 1.01, lowerBound + (diff * 0.05));
        xValue.setTextAnchor(TextAnchor.CENTER_LEFT);
        plot.addAnnotation(xValue);
        if (zeit != null) {
            final XYTextAnnotation time = new XYTextAnnotation("Zeit:     " + zeit, x * 1.01, lowerBound + (diff * 0.085));
            time.setTextAnchor(TextAnchor.CENTER_LEFT);
            plot.addAnnotation(time);
        }
    }
}
