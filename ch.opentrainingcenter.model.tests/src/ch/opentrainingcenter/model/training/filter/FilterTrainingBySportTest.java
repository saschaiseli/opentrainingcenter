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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.training.filter.internal.FilterTrainingBySport;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class FilterTrainingBySportTest {
    private FilterTrainingBySport filter;

    private ITraining training;

    @Before
    public void setUp() {
        training = mock(ITraining.class);
    }

    @Test
    public void testOnNextDoNotFilter() {
        filter = new FilterTrainingBySport(Sport.RUNNING);
        when(training.getSport()).thenReturn(Sport.RUNNING);

        final boolean result = filter.select(training);

        assertTrue(result);
    }

    @Test
    public void testOnNextDoFilter() {
        filter = new FilterTrainingBySport(Sport.BIKING);
        when(training.getSport()).thenReturn(Sport.RUNNING);

        final boolean result = filter.select(training);

        assertFalse(result);
    }

    @Test
    public void testDoFilter_Other_RUN() {
        filter = new FilterTrainingBySport(Sport.OTHER);
        when(training.getSport()).thenReturn(Sport.RUNNING);

        final boolean result = filter.select(training);

        assertTrue(result);
    }

    @Test
    public void testDoFilter_Other_BIKE() {
        filter = new FilterTrainingBySport(Sport.OTHER);
        when(training.getSport()).thenReturn(Sport.BIKING);

        final boolean result = filter.select(training);

        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnNextDoFilterWithNullType() {
        filter = new FilterTrainingBySport(null);
        when(training.getSport()).thenReturn(Sport.RUNNING);

        filter.select(training);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnNextDoFilterWithNullSport() {
        filter = new FilterTrainingBySport(Sport.RUNNING);

        filter.select(null);
    }
}
