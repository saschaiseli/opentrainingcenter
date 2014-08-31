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

package ch.opentrainingcenter.core.db;

import java.util.Set;

import ch.opentrainingcenter.core.db.criteria.DistanceCriteria;
import ch.opentrainingcenter.core.db.criteria.NoteCriteria;
import ch.opentrainingcenter.core.db.criteria.SportCriteria;
import ch.opentrainingcenter.core.db.criteria.StreckeCriteria;
import ch.opentrainingcenter.transfer.Sport;

public final class CriteriaFactory {

    private CriteriaFactory() {

    }

    public static ISearchCriteria createNoteCriteria(final String beschreibung) {
        return new NoteCriteria(beschreibung);
    }

    public static ISearchCriteria createDistanceCriteria(final int laenge) {
        return new DistanceCriteria(laenge);
    }

    public static ISearchCriteria createSportCriteria(final Set<Sport> sports) {
        return new SportCriteria(sports);
    }

    public static CriteriaContainer createCriteriaContainer() {
        return new CriteriaContainer();
    }

    public static ISearchCriteria createStreckeCriteria(final int referenzTrainingId) {
        // TODO Auto-generated method stub
        return new StreckeCriteria(referenzTrainingId);
    }
}
