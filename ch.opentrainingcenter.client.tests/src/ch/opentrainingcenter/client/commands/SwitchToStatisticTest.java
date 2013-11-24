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

public class SwitchToStatisticTest {
    private SwitchToStatistic tt;

    @Before
    public void setUp() {
        tt = new SwitchToStatistic();
    }

    @Test
    public void testGetPerspectiveId() {
        assertEquals(StatisticPerspective.ID, tt.getPerspectiveId());
    }

    @Test
    public void testIsSamePerspective() {
        assertFalse(tt.isSamePerspective(TablePerspective.ID));
        assertFalse(tt.isSamePerspective(EinstellungenPerspective.ID));
        assertFalse(tt.isSamePerspective(MainPerspective.ID));
        assertTrue(tt.isSamePerspective(StatisticPerspective.ID));
    }

}
