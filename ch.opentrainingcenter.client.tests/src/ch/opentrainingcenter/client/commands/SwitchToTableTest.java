package ch.opentrainingcenter.client.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.perspectives.EinstellungenPerspective;
import ch.opentrainingcenter.client.perspectives.MainPerspective;
import ch.opentrainingcenter.client.perspectives.StatisticPerspective;
import ch.opentrainingcenter.client.perspectives.TablePerspective;

public class SwitchToTableTest {

    private SwitchToTable tt;

    @Before
    public void setUp() {
        tt = new SwitchToTable();
    }

    @Test
    public void testGetPerspectiveId() {
        assertEquals(TablePerspective.ID, tt.getPerspectiveId());
    }

    @Test
    public void testIsSamePerspective() {
        assertTrue(tt.isSamePerspective(TablePerspective.ID));
        assertFalse(tt.isSamePerspective(EinstellungenPerspective.ID));
        assertFalse(tt.isSamePerspective(MainPerspective.ID));
        assertFalse(tt.isSamePerspective(StatisticPerspective.ID));
    }

}
