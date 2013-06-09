package ch.opentrainingcenter.core.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;

public class RunTypeTest {

    private RunType none;
    private RunType ext;
    private RunType inten;
    private RunType longjog;
    private RunType powerlongjog;
    private RunType temp;

    @Before
    public void before() {
        none = RunType.NONE;
        ext = RunType.EXT_INTERVALL;
        inten = RunType.INT_INTERVALL;
        longjog = RunType.LONG_JOG;
        powerlongjog = RunType.POWER_LONG_JOG;
        temp = RunType.TEMPO_JOG;
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
    public void testGetTitle() {
        assertEquals(Messages.RunType0, none.getTitle());
        assertEquals(Messages.RunType1, ext.getTitle());
        assertEquals(Messages.RunType2, inten.getTitle());
        assertEquals(Messages.RunType3, longjog.getTitle());
        assertEquals(Messages.RunType4, powerlongjog.getTitle());
        assertEquals(Messages.RunType5, temp.getTitle());
    }

    @Test
    public void testGetRunType() {
        assertEquals(none, RunType.getRunType(0));
        assertEquals(ext, RunType.getRunType(1));
        assertEquals(inten, RunType.getRunType(2));
        assertEquals(longjog, RunType.getRunType(3));
        assertEquals(powerlongjog, RunType.getRunType(4));
        assertEquals(temp, RunType.getRunType(5));
    }

    @Test
    public void testGetAllTypes() {
        final String[] allTypes = RunType.getAllTypes();
        assertEquals(6, allTypes.length);
    }

}
