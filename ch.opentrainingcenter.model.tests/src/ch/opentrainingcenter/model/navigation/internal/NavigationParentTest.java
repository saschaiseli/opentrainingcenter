package ch.opentrainingcenter.model.navigation.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.navigation.INavigationItem;

@SuppressWarnings("nls")
public class NavigationParentTest {

    private NavigationParent parent;
    private Calendar cal, cal2;

    @Before
    public void setUp() {
        parent = new NavigationParent();
        cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);
        cal2 = Calendar.getInstance(Locale.getDefault());
        cal2.set(2013, 1, 5, 14, 23, 22);
    }

    @Test
    public void testGetNameNull() {
        final String result = parent.getName();

        assertEquals("KW", result);
    }

    @Test
    public void testGetName() {
        final INavigationItem item = mock(INavigationItem.class);
        when(item.getDate()).thenReturn(cal.getTime());
        parent.add(item);

        final String result = parent.getName();

        assertEquals("KW1", result);
    }

    @Test
    public void testGetTooltip() {
        final String result = parent.getTooltip();

        assertEquals("Keine Childs keine tooltips", null, result);
    }

    @Test
    public void testGetTooltipEinChild() {
        final INavigationItem item = mock(INavigationItem.class);
        when(item.getDate()).thenReturn(cal.getTime());
        final String tooltip = "tooltip";
        when(item.getTooltip()).thenReturn(tooltip);
        parent.add(item);

        final String result = parent.getTooltip();

        assertEquals(tooltip, result);
    }

    @Test
    public void testGetTooltipZweiChild() {
        final INavigationItem item1 = mock(INavigationItem.class);
        when(item1.getDate()).thenReturn(cal.getTime());
        when(item1.getLaengeInMeter()).thenReturn(12345.678);

        final INavigationItem item2 = mock(INavigationItem.class);
        when(item2.getDate()).thenReturn(cal2.getTime());
        when(item2.getLaengeInMeter()).thenReturn(10000.000);

        parent.add(item2);
        parent.add(item1);

        final String result = parent.getTooltip();

        assertEquals("2 Records: vom 04.01.2012 bis 05.02.2013 Distanz total: 22.346km", result);
    }
}
