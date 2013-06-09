package ch.opentrainingcenter.core.helper;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class ColorFromPreferenceHelperTest {

    @Test
    public void testGetColorNichtImStore() {
        final IPreferenceStore store = Mockito.mock(IPreferenceStore.class);
        Mockito.when(store.getString((String) Matchers.any())).thenReturn(null);
        final Color color = ColorFromPreferenceHelper.getColor(store, null, 0);
        assertEquals("Keine Farbe gefunden, default ist schwarz", Color.black, color);
    }

    @Test
    public void testGetColorImStore() {
        final IPreferenceStore store = Mockito.mock(IPreferenceStore.class);
        final String key = "255,254,253";
        Mockito.when(store.getString((String) Matchers.any())).thenReturn(key);
        final Color color = ColorFromPreferenceHelper.getColor(store, key, 0);
        final Color referenz = new Color(255, 254, 253);
        assertEquals(referenz.getRed(), color.getRed());
        assertEquals(referenz.getGreen(), color.getGreen());
        assertEquals(referenz.getBlue(), color.getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetColorFehlerFall() {
        final IPreferenceStore store = Mockito.mock(IPreferenceStore.class);
        final String key = "255,254";
        Mockito.when(store.getString((String) Matchers.any())).thenReturn(key);
        ColorFromPreferenceHelper.getColor(store, key, 0);
    }

    @Test
    @Ignore
    public void testGetColorNichtImStoreSwt() {
        final IPreferenceStore store = Mockito.mock(IPreferenceStore.class);
        Mockito.when(store.getString((String) Matchers.any())).thenReturn(null);
        final org.eclipse.swt.graphics.Color color = ColorFromPreferenceHelper.getSwtColor(store, null);
        final Device device = Display.getCurrent();
        final org.eclipse.swt.graphics.Color referenz = new org.eclipse.swt.graphics.Color(device, 255, 255, 255);
        assertEquals("Keine Farbe gefunden, default ist schwarz", referenz, color);
    }

    @Test
    @Ignore
    public void testGetColorImStoreSwt() {
        final IPreferenceStore store = Mockito.mock(IPreferenceStore.class);
        final String key = "255,254,253";
        Mockito.when(store.getString((String) Matchers.any())).thenReturn(key);
        final org.eclipse.swt.graphics.Color color = ColorFromPreferenceHelper.getSwtColor(store, key);
        final Device device = Display.getCurrent();
        final org.eclipse.swt.graphics.Color referenz = new org.eclipse.swt.graphics.Color(device, 255, 254, 253);

        assertEquals("Keine Farbe gefunden, default ist schwarz", referenz, color);
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void testGetColorFehlerFallSwt() {
        final IPreferenceStore store = Mockito.mock(IPreferenceStore.class);
        final String key = "255,254";
        Mockito.when(store.getString((String) Matchers.any())).thenReturn(key);
        ColorFromPreferenceHelper.getSwtColor(store, key);
    }

}
