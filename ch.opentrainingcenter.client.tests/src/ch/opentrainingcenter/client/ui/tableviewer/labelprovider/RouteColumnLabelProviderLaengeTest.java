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
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.opentrainingcenter.client.ui.tableviewer.RoutenTableModel;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class RouteColumnLabelProviderLaengeTest {
    @Mock
    private IRoute route;
    @Mock
    private ITraining training;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_referenzTrack() {
        final RouteColumnLabelProviderLaenge provider = new RouteColumnLabelProviderLaenge();
        when(route.getReferenzTrack()).thenReturn(training);
        when(training.getLaengeInMeter()).thenReturn(1234.123);

        final String result = provider.getText(new RoutenTableModel(route, 0));

        assertEquals("1.234", result);
    }

    @Test
    public void test_referenzTrack_ist_null() {
        final RouteColumnLabelProviderLaenge provider = new RouteColumnLabelProviderLaenge();
        when(route.getReferenzTrack()).thenReturn(null);

        final String result = provider.getText(new RoutenTableModel(route, 0));

        assertEquals("", result);
    }

}
