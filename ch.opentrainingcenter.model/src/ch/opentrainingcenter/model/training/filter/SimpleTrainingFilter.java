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

package ch.opentrainingcenter.model.training.filter;

import java.util.List;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.model.training.ISimpleTraining;

public class SimpleTrainingFilter implements Filter<ISimpleTraining> {

    private final List<Filter<ISimpleTraining>> filters;

    public SimpleTrainingFilter(final List<Filter<ISimpleTraining>> filters) {
        Assertions.isValid(filters.isEmpty(), "Keine Filter im Container"); //$NON-NLS-1$
        this.filters = filters;
    }

    @Override
    public boolean select(final ISimpleTraining training) {
        Assertions.notNull(training);
        for (final Filter<ISimpleTraining> filter : filters) {
            if (!filter.select(training)) {
                return false;
            }
        }
        return true;
    }
}
