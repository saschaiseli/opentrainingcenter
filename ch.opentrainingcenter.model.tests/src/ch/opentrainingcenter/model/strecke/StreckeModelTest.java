package ch.opentrainingcenter.model.strecke;

import static org.junit.Assert.assertEquals;

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
        new StreckeModel(42, null, "abcd", "", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFullConstructorIdnull() {
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        new StreckeModel(42, athlete, null, "", 0);
    }

    @Test
    public void testConstructor() {
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        final String name = "name";
        final String beschreibung = "beschre";
        final StreckeModel model = new StreckeModel(42, athlete, name, beschreibung, 0);

        assertEquals(42, model.getId());
        assertEquals(athlete, model.getAthlete());
        assertEquals(name, model.getName());
        assertEquals(beschreibung, model.getBeschreibung());
        assertEquals(0, model.getReferenzTrainingId());
    }
}
