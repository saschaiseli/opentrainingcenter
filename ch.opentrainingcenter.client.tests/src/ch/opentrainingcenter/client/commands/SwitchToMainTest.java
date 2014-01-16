package ch.opentrainingcenter.client.commands;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.perspectives.EinstellungenPerspective;
import ch.opentrainingcenter.client.perspectives.MainPerspective;
import ch.opentrainingcenter.client.perspectives.TablePerspective;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SwitchToMainTest {
    private SwitchToMain tt;

    @Before
    public void setUp() {
        tt = new SwitchToMain();
    }

    @Test
    public void testGetPerspectiveId() {
        assertEquals(MainPerspective.ID, tt.getPerspectiveId());
    }

    @Test
    public void testIsSamePerspective() {
        assertFalse(tt.isSamePerspective(TablePerspective.ID));
        assertFalse(tt.isSamePerspective(EinstellungenPerspective.ID));
        assertTrue(tt.isSamePerspective(MainPerspective.ID));
    }

}
