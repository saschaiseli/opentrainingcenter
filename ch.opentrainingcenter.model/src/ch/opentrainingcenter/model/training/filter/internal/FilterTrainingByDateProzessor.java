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

import ch.opentrainingcenter.core.process.ElementProzessor;
import ch.opentrainingcenter.model.training.ISimpleTraining;

public class FilterTrainingByDateProzessor implements ElementProzessor<ISimpleTraining> {
    private final long startMillis;
    private final long endMillis;

    public FilterTrainingByDateProzessor(final Date start, final Date end) {
        this.startMillis = start.getTime();
        this.endMillis = end.getTime();
    }

    @Override
    public ISimpleTraining onNext(final ISimpleTraining item) {
        final long time = item.getDatum().getTime();
        if (time < startMillis || time > endMillis) {
            return null;
        }
        return item;
    }

}
