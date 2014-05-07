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

import org.junit.Test;

import ch.opentrainingcenter.core.db.CriteriaFactory;
import ch.opentrainingcenter.core.db.ISearchCriteria;
import ch.opentrainingcenter.transfer.ITraining;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class DistanceCriteriaTest {

    @Test
    public void testKleiner() {
        final ISearchCriteria criteria = CriteriaFactory.createDistanceCriteria(42);
        final ITraining training = mock(ITraining.class);
        final double distanz = 41.99999;
        when(training.getLaengeInMeter()).thenReturn(Double.valueOf(distanz));

        final boolean result = criteria.matches(training);

        assertFalse(result + "ist kleiner", result);
    }

    @Test
    public void testGleich() {
        final ISearchCriteria criteria = CriteriaFactory.createDistanceCriteria(42);
        final ITraining training = mock(ITraining.class);
        final double distanz = 42;
        when(training.getLaengeInMeter()).thenReturn(Double.valueOf(distanz));

        final boolean result = criteria.matches(training);

        assertTrue("Result ist gleich", result);
    }

    @Test
    public void testGroesser() {
        final ISearchCriteria criteria = CriteriaFactory.createDistanceCriteria(42);
        final ITraining training = mock(ITraining.class);
        final double distanz = 42.0001;
        when(training.getLaengeInMeter()).thenReturn(Double.valueOf(distanz));

        final boolean result = criteria.matches(training);

        assertTrue("Result ist groesser", result);
    }
}
