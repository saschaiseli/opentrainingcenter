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

import java.util.Date;

import ch.opentrainingcenter.charts.ng.SimpleTrainingChart;
import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.model.training.ISimpleTraining;

/**
 * Kapselt Wert(Herz- und Distanz Daten) und Category
 */
public class ChartDataWrapper implements Comparable<ChartDataWrapper> {
    private static final int KILOMETER_IN_METER = 1000;
    private final double distance;
    private final int avgHeartRate;
    private final String category;
    private final Date date;

    public ChartDataWrapper(final ISimpleTraining training, final XAxisChart type) {
        this(training.getDistanzInMeter() / KILOMETER_IN_METER, training.getAvgHeartRate(), CategoryHelper.getCategory(training.getDatum(), type), training
                .getDatum());
    }

    public ChartDataWrapper(final double distanceInMeter, final int avgHeartRate, final String category, final Date date) {
        distance = distanceInMeter;
        this.avgHeartRate = avgHeartRate;
        this.category = category;
        this.date = date;
    }

    public double getValue(final SimpleTrainingChart stc) {
        switch (stc) {
        case DISTANZ:
            return distance;
        case HERZ:
            return avgHeartRate;
        default:
            throw new IllegalArgumentException(String.format("Der Typ %s ist unbekannt", stc)); //$NON-NLS-1$
        }
    }

    public final String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(final ChartDataWrapper other) {
        return date.compareTo(other.getDate());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChartDataWrapper other = (ChartDataWrapper) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "ChartDataWrapper [category=" + category + ", date=" + date + ", distance=" + distance + ", avgHeartRate=" + avgHeartRate + "]";
    }

}
