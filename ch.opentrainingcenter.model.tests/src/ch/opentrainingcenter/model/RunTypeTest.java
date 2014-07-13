package ch.opentrainingcenter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.TrainingType;

@SuppressWarnings("nls")
public class RunTypeTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetRunType() {
        TrainingType.getByIndex(Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRunTypeNext() {
        TrainingType.getByIndex(6);
    }

    @Test
    public void testGetRunTypeValid() {
        final TrainingType runType = TrainingType.getByIndex(1);
        assertEquals(TrainingType.EXT_INTERVALL, runType);
    }

    @Test
    public void testEnumTextesAndIndezes() {
        assertEquals(Messages.RunType0, TrainingType.NONE.getName());
        assertEquals(0, TrainingType.NONE.getIndex());
        assertEquals(Messages.RunType1, TrainingType.EXT_INTERVALL.getName());
        assertEquals(1, TrainingType.EXT_INTERVALL.getIndex());
        assertEquals(Messages.RunType2, TrainingType.INT_INTERVALL.getName());
        assertEquals(2, TrainingType.INT_INTERVALL.getIndex());
        assertEquals(Messages.RunType3, TrainingType.LONG_JOG.getName());
        assertEquals(3, TrainingType.LONG_JOG.getIndex());
        assertEquals(Messages.RunType4, TrainingType.POWER_LONG_JOG.getName());
        assertEquals(4, TrainingType.POWER_LONG_JOG.getIndex());
        assertEquals(Messages.RunType5, TrainingType.TEMPO_JOG.getName());
        assertEquals(5, TrainingType.TEMPO_JOG.getIndex());
    }

    @Test
    public void testAllTypes() {
        final String[] allTypes = TrainingType.getAllTypes();
        assertNotNull(allTypes);
        assertEquals("6 Runtypes gibt es.", 6, allTypes.length);
    }
}
