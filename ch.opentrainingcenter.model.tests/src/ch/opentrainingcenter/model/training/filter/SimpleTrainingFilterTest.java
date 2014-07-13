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

import static ch.opentrainingcenter.transfer.TrainingType.EXT_INTERVALL;
import static ch.opentrainingcenter.transfer.TrainingType.INT_INTERVALL;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.training.ISimpleTraining;

public class SimpleTrainingFilterTest {

    SimpleTrainingFilter filter;
    private ISimpleTraining training;

    @Before
    public void setUp() {
        training = mock(ISimpleTraining.class);

    }

    @Test
    public void testUpperRange() {
        filter = new SimpleTrainingFilter(new Date(1), new Date(100), EXT_INTERVALL);

        when(training.getDatum()).thenReturn(new Date(100));
        when(training.getType()).thenReturn(EXT_INTERVALL);

        final ISimpleTraining result = filter.filter(training);

        assertNotNull(result);
    }

    @Test
    public void testLowerRange() {
        filter = new SimpleTrainingFilter(new Date(1), new Date(100), EXT_INTERVALL);

        when(training.getDatum()).thenReturn(new Date(1));
        when(training.getType()).thenReturn(EXT_INTERVALL);

        final ISimpleTraining result = filter.filter(training);

        assertNotNull(result);
    }

    @Test
    public void testTooLate() {
        final DateTime von = new DateTime(2012, 1, 22, 12, 22);
        final DateTime bis = new DateTime(2012, 3, 22, 12, 22);

        filter = new SimpleTrainingFilter(von.toDate(), bis.toDate(), EXT_INTERVALL);

        final DateTime dt = new DateTime(2012, 3, 23, 0, 0);

        when(training.getDatum()).thenReturn(dt.toDate());
        when(training.getType()).thenReturn(EXT_INTERVALL);

        final ISimpleTraining result = filter.filter(training);

        assertNull(result);
    }

    @Test
    public void testTooLow() {
        final DateTime von = new DateTime(2012, 1, 22, 12, 22);
        final DateTime bis = new DateTime(2012, 3, 22, 12, 22);

        filter = new SimpleTrainingFilter(von.toDate(), bis.toDate(), EXT_INTERVALL);

        final DateTime dt = new DateTime(2012, 1, 21, 23, 59);
        when(training.getDatum()).thenReturn(dt.toDate());

        final ISimpleTraining result = filter.filter(training);

        assertNull(result);
    }

    @Test
    public void testWrongType() {
        filter = new SimpleTrainingFilter(new Date(1), new Date(100), EXT_INTERVALL);

        when(training.getDatum()).thenReturn(new Date(101));
        when(training.getType()).thenReturn(INT_INTERVALL);

        final ISimpleTraining result = filter.filter(training);

        assertNull(result);
    }
}
