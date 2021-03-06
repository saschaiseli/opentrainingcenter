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

package ch.opentrainingcenter.core.db.criteria;

import ch.opentrainingcenter.core.db.ISearchCriteria;
import ch.opentrainingcenter.transfer.ITraining;

public class NoteCriteria implements ISearchCriteria {

    private final String beschreibung;

    public NoteCriteria(final String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public boolean matches(final ITraining training) {
        final String note = training.getNote();
        if (isBeschreibungValid()) {
            return true;
        } else {
            // beschreibung nicht null und nicht leer
            if (note == null) {
                return false;
            } else if (note.toUpperCase().contains(beschreibung.toUpperCase())) {
                return true;
            }

        }
        return false;
    }

    private boolean isBeschreibungValid() {
        return beschreibung == null || beschreibung.length() == 0;
    }
}
