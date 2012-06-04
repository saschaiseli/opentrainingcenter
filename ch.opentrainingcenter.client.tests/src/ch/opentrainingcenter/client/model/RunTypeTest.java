package ch.opentrainingcenter.client.model;

import org.junit.Test;

import ch.opentrainingcenter.client.Messages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RunTypeTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetRunType() {
        RunType.getRunType(Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRunTypeNext() {
        RunType.getRunType(6);
    }

    @Test
    public void testGetRunTypeValid() {
        final RunType runType = RunType.getRunType(1);
        assertEquals(RunType.EXT_INTERVALL, runType);
    }

    @Test
    public void testEnumTextesAndIndezes() {
        assertEquals(Messages.RunType0, RunType.NONE.getTitle());
        assertEquals(0, RunType.NONE.getIndex());
        assertEquals(Messages.RunType1, RunType.EXT_INTERVALL.getTitle());
        assertEquals(1, RunType.EXT_INTERVALL.getIndex());
        assertEquals(Messages.RunType2, RunType.INT_INTERVALL.getTitle());
        assertEquals(2, RunType.INT_INTERVALL.getIndex());
        assertEquals(Messages.RunType3, RunType.LONG_JOG.getTitle());
        assertEquals(3, RunType.LONG_JOG.getIndex());
        assertEquals(Messages.RunType4, RunType.POWER_LONG_JOG.getTitle());
        assertEquals(4, RunType.POWER_LONG_JOG.getIndex());
        assertEquals(Messages.RunType5, RunType.TEMPO_JOG.getTitle());
        assertEquals(5, RunType.TEMPO_JOG.getIndex());
    }

    @Test
    public void testAllTypes() {
        final String[] allTypes = RunType.getAllTypes();
        assertNotNull(allTypes);
        assertEquals("6 Runtypes gibt es.", 6, allTypes.length);
    }
}
