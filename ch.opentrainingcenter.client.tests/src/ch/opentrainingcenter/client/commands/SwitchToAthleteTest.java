package ch.opentrainingcenter.client.commands;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.perspectives.EinstellungenPerspective;
import ch.opentrainingcenter.client.perspectives.MainPerspective;
import ch.opentrainingcenter.client.perspectives.TablePerspective;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertFalse(tt.isSamePerspective(TablePerspective.ID));
        assertTrue(tt.isSamePerspective(EinstellungenPerspective.ID));
        assertFalse(tt.isSamePerspective(MainPerspective.ID));
    }

}
