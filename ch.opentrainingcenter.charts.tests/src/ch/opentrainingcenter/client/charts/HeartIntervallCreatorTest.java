package ch.opentrainingcenter.client.charts;

import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.charts.single.creators.internal.HeartIntervallCreator;
import ch.opentrainingcenter.core.helper.ZoneHelper.Zone;
import ch.opentrainingcenter.transfer.IAthlete;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class HeartIntervallCreatorTest {
    private IAthlete athlete;
    private HeartIntervallCreator creator;
    private IPreferenceStore store;

    @Before
    public void before() {
        athlete = Mockito.mock(IAthlete.class);
        store = Mockito.mock(IPreferenceStore.class);
        creator = new HeartIntervallCreator(store);
    }

    @Test
    public void test() {
        final Map<Zone, IntervalMarker> marker = creator.createMarker(athlete);
        assertNotNull(marker);
    }

    @Test
    public void testValues() {
        Mockito.when(store.getInt(Zone.GA1.getName())).thenReturn(20);
        Mockito.when(store.getInt(Zone.GA12.getName())).thenReturn(30);
        Mockito.when(store.getInt(Zone.GA2.getName())).thenReturn(40);
        Mockito.when(athlete.getMaxHeartRate()).thenReturn(Integer.valueOf(180));
        final Map<Zone, IntervalMarker> marker = creator.createMarker(athlete);
        assertEquals("5 Bereiche sollten vorhanden sein", 5, marker.size());

        final IntervalMarker aerobe = marker.get(Zone.GA1);
        assertEquals("GA 1", aerobe.getLabel());

        final IntervalMarker schwelle = marker.get(Zone.GA12);
        assertEquals("GA 1/2", schwelle.getLabel());

        final IntervalMarker anaerobe = marker.get(Zone.GA2);
        assertEquals("GA 2", anaerobe.getLabel());

        assertProperties(aerobe);
        assertProperties(schwelle);
        assertProperties(anaerobe);
    }

    private void assertProperties(final IntervalMarker marker) {
        assertEquals(RectangleAnchor.LEFT, marker.getLabelAnchor());
        assertEquals(TextAnchor.CENTER_LEFT, marker.getLabelTextAnchor());
    }
}
