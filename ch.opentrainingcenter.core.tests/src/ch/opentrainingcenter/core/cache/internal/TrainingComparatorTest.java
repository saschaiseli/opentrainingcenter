package ch.opentrainingcenter.core.cache.internal;

import org.junit.Test;

import ch.opentrainingcenter.transfer.ITraining;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class TrainingComparatorTest {
    @Test
    public void testCompare1() {
        final TrainingComparator comp = new TrainingComparator();
        final ITraining o1 = mock(ITraining.class);
        final ITraining o2 = mock(ITraining.class);

        when(o1.getDatum()).thenReturn(10L);
        when(o2.getDatum()).thenReturn(11L);

        final int result = comp.compare(o1, o2);
        assertEquals("2. Datum ist älter", 1, result);
    }

    @Test
    public void testCompareEquals() {
        final TrainingComparator comp = new TrainingComparator();
        final ITraining o1 = mock(ITraining.class);
        final ITraining o2 = mock(ITraining.class);

        when(o1.getDatum()).thenReturn(10L);
        when(o2.getDatum()).thenReturn(10L);
        final int result = comp.compare(o1, o2);
        assertEquals("Gleich gross", 0, result);
    }

    @Test
    public void testCompare2() {
        final TrainingComparator comp = new TrainingComparator();
        final ITraining o1 = mock(ITraining.class);
        final ITraining o2 = mock(ITraining.class);

        when(o1.getDatum()).thenReturn(11L);
        when(o2.getDatum()).thenReturn(10L);
        final int result = comp.compare(o1, o2);
        assertEquals("1. datum ist älter", -1, result);
    }
}
