package ch.opentrainingcenter.client.helper;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.model.planing.impl.KwJahrKey;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class KwJahrKeyComparatorTest {
    private KwJahrKeyComparator comp;
    private KwJahrKey o1;
    private KwJahrKey o2;

    @Before
    public void before() {
        comp = new KwJahrKeyComparator();
    }

    @Test
    public void test0() {
        o1 = new KwJahrKey(2012, 10);
        o2 = new KwJahrKey(2012, 11);
        final int compare = comp.compare(o1, o2);
        assertEquals("KW10 (erstes Element) ist kleiner", -1, compare);
    }

    @Test
    public void test1() {
        o1 = new KwJahrKey(2012, 10);
        o2 = new KwJahrKey(2012, 11);
        final int compare = comp.compare(o2, o1);
        assertEquals("KW10 (2 Element) ist kleiner", 1, compare);
    }

    @Test
    public void test2() {
        o1 = new KwJahrKey(2013, 10);
        o2 = new KwJahrKey(2012, 11);
        final int compare = comp.compare(o1, o2);
        assertEquals("2013 ist grösser", 1, compare);
    }

    @Test
    public void test4() {
        o1 = new KwJahrKey(2013, 10);
        o2 = new KwJahrKey(2013, 10);
        final int compare = comp.compare(o1, o2);
        assertEquals("gleich grosse", 0, compare);
    }
}
