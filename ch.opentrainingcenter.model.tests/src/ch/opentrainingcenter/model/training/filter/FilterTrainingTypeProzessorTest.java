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

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.core.process.ElementProzessor;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.model.training.filter.internal.FilterTrainingTypeProzessor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FilterTrainingTypeProzessorTest {
    private FilterTrainingTypeProzessor prozessor;
    private ElementProzessor<ISimpleTraining> next;

    private ISimpleTraining training;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        next = mock(ElementProzessor.class);
        training = mock(ISimpleTraining.class);
    }

    @Test
    public void testOnNextDoNotFilter() {
        prozessor = new FilterTrainingTypeProzessor(next, RunType.EXT_INTERVALL);
        when(training.getType()).thenReturn(RunType.EXT_INTERVALL);

        prozessor.onNext(training);

        verify(next).onNext(training);
    }

    @Test
    public void testOnNextDoFilter() {
        prozessor = new FilterTrainingTypeProzessor(next, RunType.EXT_INTERVALL);
        when(training.getType()).thenReturn(RunType.INT_INTERVALL);

        prozessor.onNext(training);

        verifyZeroInteractions(next);
    }

    @Test
    public void testOnNextDoFilterWithNullType() {
        prozessor = new FilterTrainingTypeProzessor(next, null);
        when(training.getType()).thenReturn(RunType.INT_INTERVALL);

        prozessor.onNext(training);

        verify(next).onNext(training);
    }
}
