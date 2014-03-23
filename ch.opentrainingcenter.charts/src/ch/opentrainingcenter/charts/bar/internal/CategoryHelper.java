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
import java.util.Date;
import java.util.Locale;

import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.i18n.Messages;

public final class CategoryHelper {

    private CategoryHelper() {
    }

    public static String getCategory(final Date date, final XAxisChart type) {
        switch (type) {
        case DAY:
            return String.valueOf(createDate(date).get(Calendar.DAY_OF_YEAR));
        case WEEK:
            return getWeekCategory(date);
        case MONTH:
            return getMonthCategory(date);
        case YEAR:
            return String.valueOf(createDate(date).get(Calendar.YEAR));
        default:
            throw new IllegalArgumentException(String.format("Die Kategorie %s ist nicht abgebildet", type)); //$NON-NLS-1$
        }
    }

    private static String getWeekCategory(final Date date) {
        String week = String.valueOf(createDate(date).get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1) {
            week = "0" + week; //$NON-NLS-1$
        }
        return week;
    }

    private static String getMonthCategory(final Date date) {
        final int i = createDate(date).get(Calendar.MONTH);
        switch (i) {
        case 0:
            return Messages.CategoryHelper_4;
        case 1:
            return Messages.CategoryHelper_5;
        case 2:
            return Messages.CategoryHelper_6;
        case 3:
            return Messages.CategoryHelper_7;
        case 4:
            return Messages.CategoryHelper_8;
        case 5:
            return Messages.CategoryHelper_9;
        case 6:
            return Messages.CategoryHelper_10;
        case 7:
            return Messages.CategoryHelper_11;
        case 8:
            return Messages.CategoryHelper_12;
        case 9:
            return Messages.CategoryHelper_13;
        case 10:
            return Messages.CategoryHelper_14;
        case 11:
            return Messages.CategoryHelper_15;
        default:
            return Messages.CategoryHelper_16;
        }

    }

    public static String getDomainAxis(final XAxisChart type) {
        switch (type) {
        case DAY:
            return Messages.CategoryHelper_0;
        case MONTH:
            return Messages.CategoryHelper_1;
        case WEEK:
            return Messages.CategoryHelper_2;
        case YEAR:
            return Messages.CategoryHelper_3;
        default:
            throw new IllegalArgumentException(String.format("Der Typ %s konnte nicht zugeordnet werden", type)); //$NON-NLS-1$
        }
    }

    private static Calendar createDate(final Date date) {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        return cal;
    }
}
