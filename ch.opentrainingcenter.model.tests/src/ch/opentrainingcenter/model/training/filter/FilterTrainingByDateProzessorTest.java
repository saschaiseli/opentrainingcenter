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

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.model.training.filter.internal.FilterTrainingByDateProzessor;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilterTrainingByDateProzessorTest {
    FilterTrainingByDateProzessor prozessor;
    private ISimpleTraining item;

    @Before
    public void setUp() {

        item = mock(ISimpleTraining.class);
    }

    @Test
    public void testUpperMargin() {
        prozessor = new FilterTrainingByDateProzessor(new Date(1), new Date(1000));

        when(item.getDatum()).thenReturn(new Date(1000));

        final ISimpleTraining result = prozessor.onNext(item);

        assertNotNull(result);
    }

    @Test
    public void testLowerMargin() {
        prozessor = new FilterTrainingByDateProzessor(new Date(1), new Date(1000));

        when(item.getDatum()).thenReturn(new Date(1));

        final ISimpleTraining result = prozessor.onNext(item);

        assertNotNull(result);
    }

    @Test
    public void testTooLow() {
        final DateTime von = new DateTime(2012, 1, 22, 12, 22);
        final DateTime bis = new DateTime(2012, 3, 22, 12, 22);
        prozessor = new FilterTrainingByDateProzessor(von.toDate(), bis.toDate());
        final DateTime training = new DateTime(2012, 1, 21, 23, 59);
        when(item.getDatum()).thenReturn(training.toDate());

        final ISimpleTraining result = prozessor.onNext(item);

        assertNull(result);
    }

    @Test
    public void testTooLate() {
        final DateTime von = new DateTime(2012, 1, 22, 12, 22);
        final DateTime bis = new DateTime(2012, 3, 22, 12, 22);
        prozessor = new FilterTrainingByDateProzessor(von.toDate(), bis.toDate());
        final DateTime training = new DateTime(2012, 3, 23, 0, 0);
        when(item.getDatum()).thenReturn(training.toDate());

        final ISimpleTraining result = prozessor.onNext(item);

        assertNull(result);
    }

    @Test
    public void testRoundedByDay() {
        final DateTime von = new DateTime(2012, 1, 22, 12, 22);
        final DateTime bis = new DateTime(2012, 3, 22, 12, 22);
        prozessor = new FilterTrainingByDateProzessor(von.toDate(), bis.toDate());
        final DateTime training = new DateTime(2012, 3, 22, 16, 22);
        when(item.getDatum()).thenReturn(training.toDate());

        final ISimpleTraining result = prozessor.onNext(item);

        assertNotNull(result);
    }
}
