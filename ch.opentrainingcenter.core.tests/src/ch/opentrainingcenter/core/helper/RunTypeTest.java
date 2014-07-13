package ch.opentrainingcenter.core.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.TrainingType;

public class RunTypeTest {

    private TrainingType none;
    private TrainingType ext;
    private TrainingType inten;
    private TrainingType longjog;
    private TrainingType powerlongjog;
    private TrainingType temp;

    @Before
    public void before() {
        none = TrainingType.NONE;
        ext = TrainingType.EXT_INTERVALL;
        inten = TrainingType.INT_INTERVALL;
        longjog = TrainingType.LONG_JOG;
        powerlongjog = TrainingType.POWER_LONG_JOG;
        temp = TrainingType.TEMPO_JOG;
    }

    @Test
    public void testGetIndex() {
        assertEquals(0, none.getIndex());
        assertEquals(1, ext.getIndex());
        assertEquals(2, inten.getIndex());
        assertEquals(3, longjog.getIndex());
        assertEquals(4, powerlongjog.getIndex());
        assertEquals(5, temp.getIndex());
    }

    @Test
    public void testgetName() {
        assertEquals(Messages.RunType0, none.getName());
        assertEquals(Messages.RunType1, ext.getName());
        assertEquals(Messages.RunType2, inten.getName());
        assertEquals(Messages.RunType3, longjog.getName());
        assertEquals(Messages.RunType4, powerlongjog.getName());
        assertEquals(Messages.RunType5, temp.getName());
    }

    @Test
    public void testGetTrainingType() {
        assertEquals(none, TrainingType.getByIndex(0));
        assertEquals(ext, TrainingType.getByIndex(1));
        assertEquals(inten, TrainingType.getByIndex(2));
        assertEquals(longjog, TrainingType.getByIndex(3));
        assertEquals(powerlongjog, TrainingType.getByIndex(4));
        assertEquals(temp, TrainingType.getByIndex(5));
    }

    @Test
    public void testGetAllTypes() {
        final String[] allTypes = TrainingType.getAllTypes();
        assertEquals(6, allTypes.length);
    }

}
