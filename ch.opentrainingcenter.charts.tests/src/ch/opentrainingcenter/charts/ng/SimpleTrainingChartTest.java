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

package ch.opentrainingcenter.charts.ng;

import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleTrainingChartTest {

    @Test
    public void testDistanz() {
        assertEquals(0, SimpleTrainingChart.DISTANZ.getIndex());
        assertEquals(Messages.SimpleTrainingChart_0, SimpleTrainingChart.DISTANZ.getTitle());
        assertEquals(Messages.SimpleTrainingChart_1, SimpleTrainingChart.DISTANZ.getxAchse());
        assertEquals(Messages.SimpleTrainingChart_2, SimpleTrainingChart.DISTANZ.getyAchse());
        assertEquals(Messages.SimpleTrainingChart_3, SimpleTrainingChart.DISTANZ.getComboTitle());
    }

    @Test
    public void testHerz() {
        assertEquals(1, SimpleTrainingChart.HERZ.getIndex());
        assertEquals(Messages.SimpleTrainingChart_4, SimpleTrainingChart.HERZ.getTitle());
        assertEquals(Messages.SimpleTrainingChart_5, SimpleTrainingChart.HERZ.getxAchse());
        assertEquals(Messages.SimpleTrainingChart_6, SimpleTrainingChart.HERZ.getyAchse());
        assertEquals(Messages.SimpleTrainingChart_7, SimpleTrainingChart.HERZ.getComboTitle());
    }

    @Test
    public void testByIndex() {
        assertEquals(SimpleTrainingChart.DISTANZ, SimpleTrainingChart.getByIndex(0));
        assertEquals(SimpleTrainingChart.HERZ, SimpleTrainingChart.getByIndex(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByIndexUnknown() {
        SimpleTrainingChart.getByIndex(2);
    }

    @Test
    public void testItems() {
        assertNotNull(SimpleTrainingChart.items());
        assertEquals(2, SimpleTrainingChart.items().length);
    }
}
