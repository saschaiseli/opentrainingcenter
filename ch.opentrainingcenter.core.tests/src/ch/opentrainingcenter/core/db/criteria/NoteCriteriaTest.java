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

import ch.opentrainingcenter.core.db.CriteriaFactory;
import ch.opentrainingcenter.core.db.ISearchCriteria;
import ch.opentrainingcenter.transfer.ITraining;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class NoteCriteriaTest {

    private ITraining training;

    @Before
    public void setUp() {
        training = mock(ITraining.class);
    }

    @Test
    public void testMatchesWithNull() {
        when(training.getNote()).thenReturn(null);

        final ISearchCriteria criteria = CriteriaFactory.createNoteCriteria("dummy");

        assertFalse("Dummy in Beschreibung nicht gefunden", criteria.matches(training));
    }

    @Test
    public void testMatchesWithEmpty() {
        when(training.getNote()).thenReturn("");

        final ISearchCriteria criteria = CriteriaFactory.createNoteCriteria("dummy");

        assertFalse("Dummy in Beschreibung nicht gefunden", criteria.matches(training));
    }

    @Test
    public void testMatchesIgnoreCase() {
        when(training.getNote()).thenReturn("DUMMY");

        final ISearchCriteria criteria = CriteriaFactory.createNoteCriteria("dummy");

        assertTrue("Text gefunden", criteria.matches(training));
    }

    @Test
    public void testMatchesPartOf() {
        when(training.getNote()).thenReturn("DUMMYist super");

        final ISearchCriteria criteria = CriteriaFactory.createNoteCriteria("dummy");

        assertTrue("Text gefunden", criteria.matches(training));
    }

    @Test
    public void testMatchesPartOf_before() {
        when(training.getNote()).thenReturn("jajaDUMMYist super");

        final ISearchCriteria criteria = CriteriaFactory.createNoteCriteria("dummy");

        assertTrue("Text gefunden", criteria.matches(training));
    }

    @Test
    public void testMatchesWithNullSearchString() {
        when(training.getNote()).thenReturn("jajaDUMMYist super");

        final ISearchCriteria criteria = CriteriaFactory.createNoteCriteria(null);

        assertTrue("Bei leerem input(null) wird das training immer gefunden", criteria.matches(training));
    }

    @Test
    public void testMatchesWithLeeremSearchString() {
        when(training.getNote()).thenReturn("jajaDUMMYist super");

        final ISearchCriteria criteria = CriteriaFactory.createNoteCriteria("");

        assertTrue("Bei leerem input wird das training immer gefunden", criteria.matches(training));
    }

    @Test
    public void testMatchesWithSearchString() {
        when(training.getNote()).thenReturn("jajaDUMMYist super");

        final ISearchCriteria criteria = CriteriaFactory.createNoteCriteria("U");

        assertTrue("training wird gefunden", criteria.matches(training));
    }
}
