package ch.opentrainingcenter.client.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.perspectives.EinstellungenPerspective;
import ch.opentrainingcenter.client.perspectives.MainPerspective;

public class SwitchToAthleteTest {

    private SwitchToAthlete tt;

    @Before
    public void setUp() {
        tt = new SwitchToAthlete();
    }

    @Test
    public void testGetPerspectiveId() {
        assertEquals(EinstellungenPerspective.ID, tt.getPerspectiveId());
    }

    @Test
    public void testIsSamePerspective() {
        assertTrue(tt.isSamePerspective(EinstellungenPerspective.ID));
        assertFalse(tt.isSamePerspective(MainPerspective.ID));
    }

}
