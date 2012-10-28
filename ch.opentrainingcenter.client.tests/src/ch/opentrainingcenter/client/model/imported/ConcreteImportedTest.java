package ch.opentrainingcenter.client.model.imported;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.model.navigation.impl.ConcreteImported;
import ch.opentrainingcenter.transfer.IImported;
import static org.junit.Assert.assertEquals;

public class ConcreteImportedTest {
    @Test
    public void testOrder() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);

        final IImported imported1 = Mockito.mock(IImported.class);
        Mockito.when(imported1.getActivityId()).thenReturn(cal.getTime());
        final ConcreteImported imp1 = new ConcreteImported(imported1);

        final Calendar cal2 = Calendar.getInstance(Locale.getDefault());
        cal2.set(2012, 0, 5, 13, 22, 59);

        final IImported imported2 = Mockito.mock(IImported.class);
        Mockito.when(imported2.getActivityId()).thenReturn(cal2.getTime());
        final ConcreteImported imp2 = new ConcreteImported(imported2);

        assertEquals("Der erste Record ist älter, daher auch grösser", 1, imp1.compareTo(imp2));
    }
}
