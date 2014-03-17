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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.model.training.ISimpleTraining;

public class ChartDataSupport {

    private final XAxisChart type;

    public ChartDataSupport(final XAxisChart type) {
        this.type = type;
    }

    /**
     * Konvertiert und sortiert die {@link ISimpleTraining} zu
     * {@link ChartDataWrapper}. Der {@link ChartDataWrapper} beinhaltet puls
     * und distanz daten.
     */
    public List<ChartDataWrapper> convertAndSort(final List<ISimpleTraining> data) {
        final List<ChartDataWrapper> result = new ArrayList<>();
        Assertions.notNull(data);
        for (final ISimpleTraining training : data) {
            result.add(new ChartDataWrapper(training, type));
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Kreiert aus den NOW Daten die PAST Daten. Basierend auf den NOW Daten
     * werden nichtexistente PAST Daten mit null Werten erzeugt.
     */
    public List<ChartDataWrapper> createPastData(final List<ISimpleTraining> dataPast, final List<ChartDataWrapper> now) {
        Assertions.notNull(dataPast);
        Assertions.notNull(now);
        final List<ChartDataWrapper> past = new ArrayList<>();
        if (!XAxisChart.DAY.equals(type)) {
            past.addAll(convertAndSort(dataPast));
            adjust(past, now);
            Collections.sort(past);
        }
        return past;
    }

    private void adjust(final List<ChartDataWrapper> past, final List<ChartDataWrapper> now) {
        final Set<String> allCategories = new HashSet<>();
        for (final ChartDataWrapper vm : past) {
            allCategories.add(vm.getCategory());
        }
        final List<ChartDataWrapper> list = new ArrayList<>();
        for (final ChartDataWrapper vm : now) {
            final boolean added = allCategories.add(vm.getCategory());
            if (added) {
                // existierte noch nicht
                DateTime dt = new DateTime(vm.getDate().getTime());
                dt = dt.minusYears(1);
                list.add(new ChartDataWrapper(0d, 0, vm.getCategory(), dt.toDate()));
            }
        }
        past.addAll(list);
    }
}
