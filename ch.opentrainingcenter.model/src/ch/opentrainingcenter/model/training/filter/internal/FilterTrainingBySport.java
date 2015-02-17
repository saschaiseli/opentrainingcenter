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

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.model.training.filter.Filter;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class FilterTrainingBySport implements Filter<ITraining> {

    private final Sport sport;

    public FilterTrainingBySport(final Sport sport) {
        Assertions.notNull(sport);
        this.sport = sport;
    }

    @Override
    public boolean matches(final ITraining item) {
        Assertions.notNull(item);
        if (Sport.OTHER.equals(sport)) {
            return true;
        }
        return sport.equals(item.getSport());
    }

}
