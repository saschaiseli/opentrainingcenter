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

package ch.opentrainingcenter.model.planing;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class PastPlanungSupportTest {

    private IPastPlanung woche;
    private IPlanungWoche planung;

    @Before
    public void setUp() {
        woche = mock(IPastPlanung.class);
        planung = mock(IPlanungWoche.class);
        when(woche.getPlanung()).thenReturn(planung);

    }

    @Test
    public void testGetKmProWoche_groesser_0() {
        when(planung.getKmProWoche()).thenReturn(Integer.valueOf(42));

        final String result = PastPlanungSupport.getKmProWoche(woche);

        assertEquals("42", result);
    }

    @Test
    public void testGetKmProWoche__0() {
        when(planung.getKmProWoche()).thenReturn(Integer.valueOf(0));

        final String result = PastPlanungSupport.getKmProWoche(woche);

        assertEquals("", result);
    }

    @Test
    public void testgetLangerLauf_groesser_0() {
        when(planung.getLangerLauf()).thenReturn(Integer.valueOf(42));

        final String result = PastPlanungSupport.getLangerLauf(woche);

        assertEquals("42", result);
    }

    @Test
    public void testgetLangerLauf_0() {
        when(planung.getLangerLauf()).thenReturn(Integer.valueOf(0));

        final String result = PastPlanungSupport.getLangerLauf(woche);

        assertEquals("", result);
    }

    @Test
    public void testGetIntervall_km_pro_woche_intervall() {
        Locale.setDefault(Locale.GERMAN);
        when(planung.getKmProWoche()).thenReturn(Integer.valueOf(42));
        when(planung.isInterval()).thenReturn(true);

        final String result = PastPlanungSupport.getIntervall(woche);

        assertEquals(Messages.Common_YES, result);
    }

    @Test
    public void testGetIntervall_km_pro_woche_kein_intervall() {
        Locale.setDefault(Locale.GERMAN);
        when(planung.getKmProWoche()).thenReturn(Integer.valueOf(42));
        when(planung.isInterval()).thenReturn(false);

        final String result = PastPlanungSupport.getIntervall(woche);

        assertEquals(Messages.Common_NO, result);
    }

    @Test
    public void testGetIntervall_km_pro_woche_0_intervall() {
        Locale.setDefault(Locale.GERMAN);
        when(planung.getKmProWoche()).thenReturn(Integer.valueOf(0));
        when(planung.isInterval()).thenReturn(true);

        final String result = PastPlanungSupport.getIntervall(woche);

        assertEquals("", result);
    }

    @Test
    public void testGetIntervall_km_pro_woche_0_kein_intervall() {
        Locale.setDefault(Locale.GERMAN);
        when(planung.getKmProWoche()).thenReturn(Integer.valueOf(0));
        when(planung.isInterval()).thenReturn(false);

        final String result = PastPlanungSupport.getIntervall(woche);

        assertEquals("", result);
    }
}
