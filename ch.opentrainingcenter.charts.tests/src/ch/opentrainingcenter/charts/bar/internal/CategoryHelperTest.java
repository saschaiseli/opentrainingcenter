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

import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.i18n.Messages;
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

        final String category = CategoryHelper.getCategory(cal.getTime(), XAxisChart.DAY);

        assertEquals(String.valueOf(cal.get(Calendar.DAY_OF_YEAR)), category);
    }

    @Test
    public void testWeek() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(Calendar.WEEK_OF_YEAR, 7);// Februar

        final String category = CategoryHelper.getCategory(cal.getTime(), XAxisChart.WEEK);

        assertEquals("07", category);
    }

    @Test
    public void testMonth() {
        assertMonth(0, "Januar");
        assertMonth(1, "Februar");
        assertMonth(2, "März");
        assertMonth(3, "April");
        assertMonth(4, "Mai");
        assertMonth(5, "Juni");
        assertMonth(6, "Juli");
        assertMonth(7, "August");
        assertMonth(8, "September");
        assertMonth(9, "Oktober");
        assertMonth(10, "November");
        assertMonth(11, "Dezember");
    }

    private void assertMonth(final int month, final String monat) {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 10);
        // execute
        final String category = CategoryHelper.getCategory(cal.getTime(), XAxisChart.MONTH);
        // assert
        assertEquals(monat, category);
    }

    @Test
    public void testMonthDezember() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(Calendar.MONTH, 11);// Dez

        final String category = CategoryHelper.getCategory(cal.getTime(), XAxisChart.MONTH);

        assertEquals("Dezember", category);
    }

    @Test
    public void testYear() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(Calendar.YEAR, 2014);// Dez

        final String category = CategoryHelper.getCategory(cal.getTime(), XAxisChart.YEAR);

        assertEquals("2014", category);
    }

    @Test
    public void testDomainAxisDay() {
        final String result = CategoryHelper.getDomainAxis(XAxisChart.DAY);
        assertEquals(Messages.CategoryHelper_0, result);
    }

    @Test
    public void testDomainAxisWeek() {
        final String result = CategoryHelper.getDomainAxis(XAxisChart.WEEK);
        assertEquals(Messages.CategoryHelper_2, result);
    }

    @Test
    public void testDomainAxisMonth() {
        final String result = CategoryHelper.getDomainAxis(XAxisChart.MONTH);
        assertEquals(Messages.CategoryHelper_1, result);
    }

    @Test
    public void testDomainAxisYear() {
        final String result = CategoryHelper.getDomainAxis(XAxisChart.YEAR);
        assertEquals(Messages.CategoryHelper_3, result);
    }
}
