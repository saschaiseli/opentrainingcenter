package ch.opentrainingcenter.model.strecke;

import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.transfer.IAthlete;

@SuppressWarnings("nls")
public class StreckeModelTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullAthlete() {
        new StreckeModel(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFullConstructorAthlete() {
        new StreckeModel(42, null, "abcd", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFullConstructorIdnull() {
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        new StreckeModel(42, athlete, null, "");
    }
}
