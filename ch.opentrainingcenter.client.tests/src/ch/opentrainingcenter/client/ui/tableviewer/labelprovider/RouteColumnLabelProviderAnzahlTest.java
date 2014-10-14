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

package ch.opentrainingcenter.client.ui.tableviewer.labelprovider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.opentrainingcenter.client.ui.tableviewer.RoutenTableModel;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class RouteColumnLabelProviderAnzahlTest {

    @Mock
    private IRoute route;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_keineTracks() {
        final RouteColumnLabelProviderAnzahl provider = new RouteColumnLabelProviderAnzahl();

        final String result = provider.getText(new RoutenTableModel(route, 0));

        assertEquals("0", result);
    }

    @Test
    public void test_ein_Tracks() {
        final List<ITraining> tracks = new ArrayList<>();
        final ITraining training = mock(ITraining.class);
        when(training.getRoute()).thenReturn(route);
        when(route.getName()).thenReturn("junit");
        tracks.add(training);
        final RouteColumnLabelProviderAnzahl provider = new RouteColumnLabelProviderAnzahl();

        final String result = provider.getText(new RoutenTableModel(route, 1));

        assertEquals("1", result);
    }

    @Test
    public void test_ein_Tracks_aber_ohne_Route() {
        final List<ITraining> tracks = new ArrayList<>();
        final ITraining training = mock(ITraining.class);
        when(training.getRoute()).thenReturn(null);

        when(route.getName()).thenReturn("junit");

        tracks.add(training);
        final RouteColumnLabelProviderAnzahl provider = new RouteColumnLabelProviderAnzahl();

        final String result = provider.getText(new RoutenTableModel(route, 0));

        assertEquals("0", result);
    }

    @Test
    public void test_zwei_Tracks() {
        final List<ITraining> tracks = new ArrayList<>();
        final ITraining training1 = mock(ITraining.class);
        when(training1.getRoute()).thenReturn(route);
        tracks.add(training1);

        final ITraining training2 = mock(ITraining.class);
        when(training2.getRoute()).thenReturn(route);
        tracks.add(training2);

        when(route.getName()).thenReturn("junit");

        final RouteColumnLabelProviderAnzahl provider = new RouteColumnLabelProviderAnzahl();

        final String result = provider.getText(new RoutenTableModel(route, 2));

        assertEquals("2", result);
    }

    @Test
    public void test_drei_aber_eines_mit_anderem_Track() {
        final List<ITraining> tracks = new ArrayList<>();
        final ITraining training1 = mock(ITraining.class);
        when(training1.getRoute()).thenReturn(route);
        tracks.add(training1);

        final ITraining training2 = mock(ITraining.class);
        when(training2.getRoute()).thenReturn(route);
        tracks.add(training2);

        final ITraining training3 = mock(ITraining.class);

        final IRoute route2 = mock(IRoute.class);
        when(route2.getName()).thenReturn("other");
        when(training3.getRoute()).thenReturn(route2);
        tracks.add(training3);

        when(route.getName()).thenReturn("junit");

        final RouteColumnLabelProviderAnzahl provider = new RouteColumnLabelProviderAnzahl();

        final String result = provider.getText(new RoutenTableModel(route, 2));

        assertEquals("2", result);
    }
}
