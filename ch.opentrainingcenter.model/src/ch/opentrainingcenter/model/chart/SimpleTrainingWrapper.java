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

package ch.opentrainingcenter.model.chart;

import java.util.Date;

import ch.opentrainingcenter.model.training.ISimpleTraining;

public abstract class SimpleTrainingWrapper {

    private final ISimpleTraining simpleTraining;

    public SimpleTrainingWrapper(final ISimpleTraining simpleTraining) {
        this.simpleTraining = simpleTraining;
    }

    Date getDate() {
        return simpleTraining.getDatum();
    }

    /**
     * @return den wert, der fuer die charts relevant ist.
     */
    abstract double getValue();
}
