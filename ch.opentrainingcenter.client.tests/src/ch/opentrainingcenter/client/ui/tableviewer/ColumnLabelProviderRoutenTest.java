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

package ch.opentrainingcenter.client.ui.tableviewer;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class ColumnLabelProviderRoutenTest {

    @Mock
    private ITraining training;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRouten_null() {
        final ColumnLabelProviderRouten provider = new ColumnLabelProviderRouten();
        when(training.getRoute()).thenReturn(null);

        final String result = provider.getText(training);

        assertEquals("", result);
    }

    @Test
    public void testRouten_Route() {
        final ColumnLabelProviderRouten provider = new ColumnLabelProviderRouten();
        final IRoute route = mock(IRoute.class);
        when(route.getName()).thenReturn("Junit");
        when(training.getRoute()).thenReturn(route);

        final String result = provider.getText(training);

        assertEquals("Junit", result);
    }

    @Test
    public void testRouten_ReferenzTrackRoute() {
        final ColumnLabelProviderRouten provider = new ColumnLabelProviderRouten();
        final IRoute route = mock(IRoute.class);
        when(route.getName()).thenReturn("Junit");
        when(route.getReferenzTrack()).thenReturn(training);
        when(training.getId()).thenReturn(42);

        when(training.getRoute()).thenReturn(route);

        final String result = provider.getText(training);

        assertEquals("Junit*", result);
    }

    @Test
    public void testRouten_NichtReferenzTrackRoute() {
        final ColumnLabelProviderRouten provider = new ColumnLabelProviderRouten();
        final IRoute route = mock(IRoute.class);
        when(route.getName()).thenReturn("Junit");
        final ITraining otherTraining = mock(ITraining.class);
        when(otherTraining.getId()).thenReturn(43);
        when(route.getReferenzTrack()).thenReturn(otherTraining);
        when(training.getId()).thenReturn(42);

        when(training.getRoute()).thenReturn(route);

        final String result = provider.getText(training);

        assertEquals("Junit", result);
    }
}
