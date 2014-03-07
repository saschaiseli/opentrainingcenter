/**
 *    OpenTrainingCenter
 *
 *    Copyright (C) 2014 Sascha Iseli sascha.iseli(at)gmx.ch
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.opentrainingcenter.charts.bar.internal;

import java.awt.Graphics2D;
import java.awt.geom.RectangularShape;

import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.ui.RectangleEdge;

public class OTCBarPainter extends StandardBarPainter {
    private static final long serialVersionUID = 1L;

    @Override
    public void paintBar(final Graphics2D arg0, final BarRenderer arg1, final int arg2, final int arg3, final RectangularShape arg4, final RectangleEdge arg5) {
        super.paintBar(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public void paintBarShadow(final Graphics2D arg0, final BarRenderer arg1, final int arg2, final int arg3, final RectangularShape arg4,
            final RectangleEdge arg5, final boolean arg6) {
        // // nur nicht diese hässlichen shadows
        // // do nothing
    }
}
