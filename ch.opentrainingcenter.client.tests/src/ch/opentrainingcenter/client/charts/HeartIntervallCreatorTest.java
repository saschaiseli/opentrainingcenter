package ch.opentrainingcenter.client.charts;

import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.helper.ZoneHelper.Zone;
import ch.opentrainingcenter.transfer.IAthlete;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        Mockito.when(store.getInt(Zone.AEROBE.getName())).thenReturn(20);
        Mockito.when(store.getInt(Zone.SCHWELLE.getName())).thenReturn(30);
        Mockito.when(store.getInt(Zone.ANAEROBE.getName())).thenReturn(40);
        Mockito.when(athlete.getMaxHeartRate()).thenReturn(Integer.valueOf(180));
        final Map<Zone, IntervalMarker> marker = creator.createMarker(athlete);
        assertEquals("3 Bereiche sollten vorhanden sein", 3, marker.size());

        final IntervalMarker aerobe = marker.get(Zone.AEROBE);
        assertEquals(Messages.HeartIntervallCreator_0, aerobe.getLabel());

        final IntervalMarker schwelle = marker.get(Zone.SCHWELLE);
        assertEquals(Messages.HeartIntervallCreator_1, schwelle.getLabel());

        final IntervalMarker anaerobe = marker.get(Zone.ANAEROBE);
        assertEquals(Messages.HeartIntervallCreator_2, anaerobe.getLabel());

        assertProperties(aerobe);
        assertProperties(schwelle);
        assertProperties(anaerobe);
    }

    private void assertProperties(final IntervalMarker marker) {
        assertEquals(RectangleAnchor.LEFT, marker.getLabelAnchor());
        assertEquals(TextAnchor.CENTER_LEFT, marker.getLabelTextAnchor());
    }
}
