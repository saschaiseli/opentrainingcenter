package ch.opentrainingcenter.model.strecke;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class StreckeModelComparatorTest {

    private final StreckeModelComparator comparator = new StreckeModelComparator();
    private StreckeModel a;
    private StreckeModel b;

    @Before
    public void before() {
        a = mock(StreckeModel.class);
        b = mock(StreckeModel.class);
    }

    @Test
    public void testSortAKleiner() {
        when(a.getId()).thenReturn(1);
        when(b.getId()).thenReturn(2);

        final int result = comparator.compare(a, b);

        assertEquals("a kommt vor b", -1, result);
    }

    @Test
    public void testSortAGroesser() {
        when(a.getId()).thenReturn(2);
        when(b.getId()).thenReturn(1);

        final int result = comparator.compare(a, b);

        assertEquals("b kommt vor a", 1, result);
    }

    @Test
    public void testSortGleichGross() {
        when(a.getId()).thenReturn(1);
        when(b.getId()).thenReturn(1);

        final int result = comparator.compare(a, b);

        assertEquals("a und b gleich gross", 0, result);
    }

}
