package ch.opentrainingcenter.client.cache.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.core.cache.IRecordListener;

@SuppressWarnings("nls")
public class AbstractCacheTest {

    private TestCache cache;

    public class TestCache extends AbstractCache<Integer, CacheElement> {

        @Override
        public Integer getKey(final CacheElement value) {
            return value.getK();
        }
    }

    public class CacheElement {
        private final int k;
        private final String v;

        public CacheElement(final int k, final String v) {
            this.k = k;
            this.v = v;
        }

        public String getV() {
            return v;
        }

        public int getK() {
            return k;
        }
    }

    @Before
    public void before() {
        cache = new TestCache();
    }

    @Test
    public void testSimpleAdd() {
        final CacheElement value = new CacheElement(42, "FirstElement");
        cache.add(value);
        assertEquals("Element muss im cache gefunden werden", value, cache.get(42));
    }

    @Test
    public void testAdd() {
        final CacheElement value1 = new CacheElement(42, "1Element");
        final CacheElement value2 = new CacheElement(43, "2Element");
        final CacheElement value3 = new CacheElement(44, "3Element");
        final List<CacheElement> list = new ArrayList<CacheElement>();
        list.add(value1);
        list.add(value2);
        list.add(value3);
        cache.addAll(list);
        for (final CacheElement cacheElement : list) {
            assertEquals("Element muss im cache gefunden werden", cacheElement, cache.get(cacheElement.getK()));
            assertTrue("Element muss im cache gefunden werden", cache.contains(cacheElement.getK()));
        }
    }

    @Test
    public void testSimpleRemove() {
        final CacheElement value = new CacheElement(42, "FirstElement");
        cache.add(value);
        assertEquals("Element muss im cache gefunden werden", value, cache.get(42));
        cache.remove(42);
        assertNull("Element nicht mehr im cache", cache.get(42));
    }

    @Test
    public void testRemove() {
        final CacheElement value1 = new CacheElement(42, "1Element");
        final CacheElement value2 = new CacheElement(43, "2Element");
        final CacheElement value3 = new CacheElement(44, "3Element");
        final List<CacheElement> list = new ArrayList<CacheElement>();
        list.add(value1);
        list.add(value2);
        list.add(value3);
        cache.addAll(list);
        assertEquals("3 Elemente sind nun im cache", 3, cache.size());
        cache.remove(Arrays.asList(new Integer[] { 42, 43, 44 }));
        for (final CacheElement cacheElement : list) {
            assertNull("Element nicht mehr im cache", cache.get(cacheElement.getK()));
        }
        assertEquals("Cache ist leer", 0, cache.size());
    }

    @Test
    public void testAddListener() {
        final List<CacheElement> changed = new ArrayList<AbstractCacheTest.CacheElement>();
        final List<CacheElement> deleted = new ArrayList<AbstractCacheTest.CacheElement>();
        cache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

            @Override
            public void recordChanged(final Collection<CacheElement> entry) {
                changed.addAll(entry);
            }

            @Override
            public void deleteRecord(final Collection<CacheElement> entry) {
                deleted.addAll(entry);
            }
        });
        final CacheElement value1 = new CacheElement(42, "1Element");
        cache.add(value1);
        assertEquals("Listener wurde notifiziert", value1, changed.get(0));
        cache.remove(42);
        assertEquals("Listener wurde notifiziert", value1, deleted.get(0));
    }

    @Test
    public void testAddListeners() {
        final List<CacheElement> changed = new ArrayList<AbstractCacheTest.CacheElement>();
        final List<CacheElement> deleted = new ArrayList<AbstractCacheTest.CacheElement>();
        cache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

            @Override
            public void recordChanged(final Collection<CacheElement> entry) {
                changed.addAll(entry);
            }

            @Override
            public void deleteRecord(final Collection<CacheElement> entry) {
                deleted.addAll(entry);
            }
        });

        cache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

            @Override
            public void recordChanged(final Collection<CacheElement> entry) {
                changed.addAll(entry);
            }

            @Override
            public void deleteRecord(final Collection<CacheElement> entry) {
                deleted.addAll(entry);
            }
        });
        final CacheElement value1 = new CacheElement(42, "1Element");
        cache.add(value1);
        assertEquals("2 Listener wurde notifiziert", 2, changed.size());
        cache.remove(42);
        assertEquals("2 Listener wurde notifiziert", 2, deleted.size());
    }

    @Test
    public void testNotifyListeners() {
        final Object[] value = new Object[1];
        cache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

            @Override
            public void recordChanged(final Collection<CacheElement> entry) {
                value[0] = entry;
            }

            @Override
            public void deleteRecord(final Collection<CacheElement> entry) {
                // do nothing
            }
        });

        cache.notifyListeners();
        assertNull("Listener wurde mit null notifiziert", value[0]);
    }
}
