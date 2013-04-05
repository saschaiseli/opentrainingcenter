package ch.opentrainingcenter.model.navigation;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class ConcreteImportedTest {
    @Test
    public void testOrder() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);

        final ITraining imported1 = Mockito.mock(ITraining.class);
        Mockito.when(imported1.getDatum()).thenReturn(cal.getTime().getTime());
        final ConcreteImported imp1 = new ConcreteImported(imported1);

        final Calendar cal2 = Calendar.getInstance(Locale.getDefault());
        cal2.set(2012, 0, 5, 13, 22, 59);

        final ITraining imported2 = Mockito.mock(ITraining.class);
        Mockito.when(imported2.getDatum()).thenReturn(cal2.getTime().getTime());
        final ConcreteImported imp2 = new ConcreteImported(imported2);

        assertEquals("Der erste Record ist älter, daher auch grösser", 1, imp1.compareTo(imp2));
    }
}
