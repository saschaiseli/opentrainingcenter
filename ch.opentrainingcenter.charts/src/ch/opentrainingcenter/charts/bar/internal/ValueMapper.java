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

import ch.opentrainingcenter.charts.ng.SimpleTrainingChart;
import ch.opentrainingcenter.charts.single.ChartSerieType;
import ch.opentrainingcenter.model.training.ISimpleTraining;

public class ValueMapper implements Comparable<ValueMapper> {
    private static final int KILOMETER_IN_METER = 1000;
    private final double distance;
    private final int avgHeartRate;
    private final String category;

    public ValueMapper(final ISimpleTraining training, final ChartSerieType type) {
        distance = training.getDistanzInMeter() / KILOMETER_IN_METER;
        avgHeartRate = training.getAvgHeartRate();
        category = CategoryHelper.getCategory(training.getDatum(), type);
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

    @Override
    public int compareTo(final ValueMapper other) {
        return Integer.valueOf(category).compareTo(Integer.valueOf(other.getCategory()));
    }
}
