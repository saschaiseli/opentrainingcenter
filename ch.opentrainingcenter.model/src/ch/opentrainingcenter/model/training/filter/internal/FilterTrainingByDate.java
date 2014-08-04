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

package ch.opentrainingcenter.model.training.filter.internal;

import java.util.Date;

import org.joda.time.DateTime;

import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.model.training.filter.Filter;

public class FilterTrainingByDate implements Filter<ISimpleTraining> {
    private final long startMillis;
    private final long endMillis;

    public FilterTrainingByDate(final Date start, final Date end) {
        this.startMillis = round(start, 0, 0);
        this.endMillis = round(end, 23, 59);
    }

    private long round(final Date date, final int hour, final int minute) {
        final DateTime dtStart = new DateTime(date);
        final int year = dtStart.getYear();
        final int monthOfYear = dtStart.getMonthOfYear();
        final int dayOfYear = dtStart.getDayOfMonth();
        final DateTime dt = new DateTime(year, monthOfYear, dayOfYear, hour, minute);
        return dt.getMillis();
    }

    @Override
    public boolean select(final ISimpleTraining item) {
        final long time = item.getDatum().getTime();
        if (time < startMillis || time > endMillis) {
            return false;
        }
        return true;
    }

}
