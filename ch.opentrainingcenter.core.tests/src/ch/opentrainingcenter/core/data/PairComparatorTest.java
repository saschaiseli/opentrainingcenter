package ch.opentrainingcenter.core.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PairComparatorTest {
    @Test
    public void compareTest() {
        final Pair<Long, Double> a = new Pair<>(1L, 42D);
        final Pair<Long, Double> b = new Pair<>(2L, 43D);
        final Pair<Long, Double> c = new Pair<>(3L, 43D);
        final Pair<Long, Double> d = new Pair<>(3L, 44D);

        final PairComparator comp = new PairComparator();

        assertEquals("a ist kleiner als b", -1, comp.compare(a, b));
        assertEquals("b ist kleiner als d", -1, comp.compare(b, d));
        assertEquals("a ist kleiner als d", -1, comp.compare(a, d));
        assertEquals("b ist grösser als a", 1, comp.compare(b, a));
        assertEquals("b ist gleich a", 0, comp.compare(b, c));
    }
}
