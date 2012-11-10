package ch.opentrainingcenter.model.navigation;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IHealth;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class ConcreteHealthTest {
    @Test
    public void test() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);
        final IHealth health = CommonTransferFactory.createHealth(null, 12d, 11, cal.getTime());
        final ConcreteHealth item = new ConcreteHealth(health, "");
        final String name = item.getName();
        assertEquals("04.01.2012", name);
    }

    @Test
    public void testOrder() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);
        final IHealth health = CommonTransferFactory.createHealth(null, 12d, 11, cal.getTime());
        final ConcreteHealth item = new ConcreteHealth(health, "");

        final Calendar cal2 = Calendar.getInstance(Locale.getDefault());
        cal2.set(2012, 0, 5, 13, 22, 59);
        final IHealth health2 = CommonTransferFactory.createHealth(null, 12d, 11, cal2.getTime());
        final ConcreteHealth item2 = new ConcreteHealth(health2, "");

        assertEquals("Das erste Element ist frühre, daher ist es grösser", 1, item.compareTo(item2));
    }
}
