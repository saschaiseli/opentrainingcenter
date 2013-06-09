package ch.opentrainingcenter.charts.bar.internal;

import java.awt.Graphics2D;
import java.awt.geom.RectangularShape;

import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.ui.RectangleEdge;

public class OTCBarPainter extends StandardXYBarPainter {

    private static final long serialVersionUID = 1L;

    @Override
    public void paintBar(final Graphics2D g2, final XYBarRenderer renderer, final int row, final int column, final RectangularShape bar,
            final RectangleEdge base) {
        super.paintBar(g2, renderer, row, column, bar, base);
    }

    @Override
    public void paintBarShadow(final Graphics2D g2, final XYBarRenderer renderer, final int row, final int column, final RectangularShape bar,
            final RectangleEdge base, final boolean pegShadow) {
        // nur nicht diese hässlichen shadows
        // do nothing
    }

}
