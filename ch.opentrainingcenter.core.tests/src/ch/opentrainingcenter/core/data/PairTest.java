package ch.opentrainingcenter.core.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PairTest {

    @Test
    public void testGetters() {
        final Pair<Long, Float> a = new Pair<>(1L, 42F);
        assertEquals(1L, a.getFirst(), 0.0001);
        assertEquals(42F, a.getSecond(), 0.00001);
    }

    @Test
    public void testSetters() {
        final Pair<Long, Float> a = new Pair<>(1L, 42F);
        assertEquals(1L, a.getFirst(), 0.0001);
        assertEquals(42F, a.getSecond(), 0.00001);

        a.setFirst(142L);
        a.setsetSecond(2042F);

        assertEquals(142L, a.getFirst(), 0.0001);
        assertEquals(2042F, a.getSecond(), 0.00001);
    }
}
