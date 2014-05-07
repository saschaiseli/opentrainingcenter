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

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.db.CriteriaContainer;
import ch.opentrainingcenter.core.db.CriteriaFactory;
import ch.opentrainingcenter.transfer.ITraining;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class CriteriaContainerTest {

    private ITraining training;

    @Before
    public void setUp() {
        training = mock(ITraining.class);
    }

    @Test
    public void testEinCriteriaNichtErfuellt() {
        final CriteriaContainer container = CriteriaFactory.createCriteriaContainer();

        container.addCriteria(CriteriaFactory.createDistanceCriteria(42));
        container.addCriteria(CriteriaFactory.createNoteCriteria("abc"));

        when(training.getNote()).thenReturn("abc");
        when(training.getLaengeInMeter()).thenReturn(Double.valueOf(41));

        final boolean result = container.matches(training);

        assertFalse("ein search criteria schlug fehl", result);
    }

    @Test
    public void testEinCriteriaBeideNichtErfuellt() {
        final CriteriaContainer container = CriteriaFactory.createCriteriaContainer();

        container.addCriteria(CriteriaFactory.createDistanceCriteria(42));
        container.addCriteria(CriteriaFactory.createNoteCriteria("abc"));

        when(training.getNote()).thenReturn("");
        when(training.getLaengeInMeter()).thenReturn(Double.valueOf(41));

        final boolean result = container.matches(training);

        assertFalse("beide search criteria schlug fehl", result);
    }

    @Test
    public void testEinCriteriaBeideErfuellt() {
        final CriteriaContainer container = CriteriaFactory.createCriteriaContainer();

        container.addCriteria(CriteriaFactory.createDistanceCriteria(42));
        container.addCriteria(CriteriaFactory.createNoteCriteria("abc"));

        when(training.getNote()).thenReturn("abcdef");
        when(training.getLaengeInMeter()).thenReturn(Double.valueOf(43));

        final boolean result = container.matches(training);

        assertTrue("beide search criteria ok", result);
    }
}
