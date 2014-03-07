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

import java.util.Calendar;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.charts.single.ChartSerieType;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class CategoryHelperTest {

    @Before
    public void before() {
        Locale.setDefault(Locale.GERMAN);
    }

    @Test
    public void testDay() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(Calendar.DAY_OF_MONTH, 11);// 11
        cal.set(Calendar.MONTH, 11);// dezember

        final String category = CategoryHelper.getCategory(cal.getTime(), ChartSerieType.DAY);

        assertEquals(String.valueOf(cal.get(Calendar.DAY_OF_YEAR)), category);
    }

    @Test
    public void testWeek() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(Calendar.WEEK_OF_YEAR, 7);// Februar

        final String category = CategoryHelper.getCategory(cal.getTime(), ChartSerieType.WEEK);

        assertEquals("07", category);
    }

    @Test
    public void testMonthFeb() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(Calendar.MONTH, 1);// Februar

        final String category = CategoryHelper.getCategory(cal.getTime(), ChartSerieType.MONTH);

        assertEquals("02", category);
    }

    @Test
    public void testMonthDezember() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(Calendar.MONTH, 11);// Dez

        final String category = CategoryHelper.getCategory(cal.getTime(), ChartSerieType.MONTH);

        assertEquals("12", category);
    }

    @Test
    public void testYear() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(Calendar.YEAR, 2014);// Dez

        final String category = CategoryHelper.getCategory(cal.getTime(), ChartSerieType.YEAR);

        assertEquals("2014", category);
    }
}
