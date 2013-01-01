package ch.opentrainingcenter.core.cache.internal;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class ImportedComparatorTest {
    @Test
    public void testCompare1() {
        final ImportedComparator comp = new ImportedComparator();
        final Date o1 = new Date(10);
        final Date o2 = new Date(11);
        final int result = comp.compare(o1, o2);
        assertEquals("2. Datum ist älter", 1, result);
    }

    @Test
    public void testCompareEquals() {
        final ImportedComparator comp = new ImportedComparator();
        final Date o1 = new Date(10);
        final Date o2 = new Date(10);
        final int result = comp.compare(o1, o2);
        assertEquals("Gleich gross", 0, result);
    }

    @Test
    public void testCompare2() {
        final ImportedComparator comp = new ImportedComparator();
        final Date o1 = new Date(11);
        final Date o2 = new Date(10);
        final int result = comp.compare(o1, o2);
        assertEquals("1. datum ist älter", -1, result);
    }
}
