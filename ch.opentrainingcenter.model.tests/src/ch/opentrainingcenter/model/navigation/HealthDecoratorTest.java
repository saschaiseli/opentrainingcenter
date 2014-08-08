package ch.opentrainingcenter.model.navigation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.Test;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class HealthDecoratorTest {

    @Test
    public void testGet() {
        final IAthlete athlete = mock(IAthlete.class);
        final int cardio = 180;
        final double weight = 42d;
        final Date dateofmeasure = new Date(1_000_000_000);
        final IHealth health = CommonTransferFactory.createHealth(athlete, weight, cardio, dateofmeasure);
        final HealthDecorator decorator = new ConcreteHealth(health, "");

        assertEquals(athlete, decorator.getAthlete());
        assertEquals(cardio, decorator.getCardio().intValue());
        assertEquals(weight, decorator.getWeight().doubleValue(), 0.000001);
        assertEquals(dateofmeasure, decorator.getDateofmeasure());
    }

    @Test
    public void testSet() {
        final IAthlete athlete = mock(IAthlete.class);
        final int cardio = 180;
        final double weight = 42d;
        final Date dateofmeasure = new Date(1_000_000_000);
        final IHealth health = CommonTransferFactory.createHealth(athlete, weight, cardio, dateofmeasure);
        final HealthDecorator decorator = new ConcreteHealth(health, "");

        final IAthlete athlete2 = mock(IAthlete.class);
        final int cardio2 = 190;
        final double weight2 = 142d;
        final Date dateofmeasure2 = new Date(2_000_000_000);

        decorator.setId(42);
        decorator.setAthlete(athlete2);
        decorator.setCardio(cardio2);
        decorator.setWeight(weight2);
        decorator.setDateofmeasure(dateofmeasure2);

        assertEquals(42, decorator.getId());
        assertEquals(athlete2, decorator.getAthlete());
        assertEquals(cardio2, decorator.getCardio().intValue());
        assertEquals(weight2, decorator.getWeight().doubleValue(), 0.000001);
        assertEquals(dateofmeasure2, decorator.getDateofmeasure());
    }
}
