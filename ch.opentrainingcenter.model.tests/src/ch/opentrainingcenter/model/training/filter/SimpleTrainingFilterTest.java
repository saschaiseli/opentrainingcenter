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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.training.ISimpleTraining;

@SuppressWarnings("unchecked")
public class SimpleTrainingFilterTest {

    SimpleTrainingFilter filter;
    List<Filter<ISimpleTraining>> filters;
    private ISimpleTraining training;

    @Before
    public void setUp() {
        training = mock(ISimpleTraining.class);
        filters = new ArrayList<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterLeer() {
        filter = new SimpleTrainingFilter(filters);

        filter.select(training);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrainingNull() {
        final Filter<ISimpleTraining> filterA = mock(Filter.class);
        filters.add(filterA);
        filter = new SimpleTrainingFilter(filters);

        filter.select(null);
    }

    @Test
    public void testEinerOk() {
        filters.add(createFilter(true));
        filter = new SimpleTrainingFilter(filters);

        final boolean result = filter.select(training);

        assertTrue(result);
    }

    @Test
    public void testEinerNOk() {
        filters.add(createFilter(false));
        filter = new SimpleTrainingFilter(filters);

        final boolean result = filter.select(training);

        assertFalse(result);
    }

    @Test
    public void testZweiOk() {
        filters.add(createFilter(true));
        filters.add(createFilter(true));
        filter = new SimpleTrainingFilter(filters);

        final boolean result = filter.select(training);

        assertTrue(result);
    }

    @Test
    public void testEinerAusZweiOk() {
        filters.add(createFilter(false));
        filters.add(createFilter(true));
        filter = new SimpleTrainingFilter(filters);

        final boolean result = filter.select(training);

        assertFalse(result);
    }

    private Filter<ISimpleTraining> createFilter(final boolean value) {
        final Filter<ISimpleTraining> mock = mock(Filter.class);
        when(mock.select((ISimpleTraining) any())).thenReturn(value);
        return mock;
    }

    @Test
    public void testKeinerAusZweiOk() {
        filters.add(createFilter(false));
        filters.add(createFilter(false));
        filter = new SimpleTrainingFilter(filters);

        final boolean result = filter.select(training);

        assertFalse(result);
    }
}
