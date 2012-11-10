package ch.opentrainingcenter.model.planing;

import org.junit.Test;

import ch.opentrainingcenter.model.planing.KwJahrKey;

import static org.junit.Assert.assertEquals;

public class KwJahrKeyTest {
    @Test
    public void compareTest() {
        final KwJahrKey key1 = new KwJahrKey(2012, 34);
        final KwJahrKey key2 = new KwJahrKey(2012, 34);
        assertEquals(0, key1.compareTo(key2));
        assertEquals(0, key2.compareTo(key1));
    }

    @Test
    public void compareTestJahr() {
        final KwJahrKey key1 = new KwJahrKey(2011, 34);
        final KwJahrKey key2 = new KwJahrKey(2012, 34);
        assertEquals(-1, key1.compareTo(key2));
        assertEquals(1, key2.compareTo(key1));
    }

    @Test
    public void compareTestJahr2() {
        final KwJahrKey key1 = new KwJahrKey(2011, 31);
        final KwJahrKey key2 = new KwJahrKey(2012, 34);
        assertEquals(-1, key1.compareTo(key2));
        assertEquals(1, key2.compareTo(key1));
    }

    @Test
    public void compareTestJahr3() {
        final KwJahrKey key1 = new KwJahrKey(2012, 31);
        final KwJahrKey key2 = new KwJahrKey(2012, 34);
        assertEquals(-1, key1.compareTo(key2));
        assertEquals(1, key2.compareTo(key1));
    }
}
