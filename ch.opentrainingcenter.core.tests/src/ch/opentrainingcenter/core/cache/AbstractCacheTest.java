package ch.opentrainingcenter.core.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.transfer.ITraining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
public class AbstractCacheTest {

    class JunitListener implements IRecordListener<ITraining> {

        private final Collection<ITraining> changed = new ArrayList<>();
        private final Collection<ITraining> deleted = new ArrayList<>();
        private int recordChangeCallCounter = 0;
        private int deleteCallCounter = 0;

        @Override
        public void recordChanged(final Collection<ITraining> entry) {
            if (entry != null) {
                changed.addAll(entry);
            }
            recordChangeCallCounter++;
        }

        @Override
        public void deleteRecord(final Collection<ITraining> entry) {
            deleted.addAll(entry);
            deleteCallCounter++;
        }

        public Collection<ITraining> getChanged() {
            return changed;
        }

        public Collection<ITraining> getDeleted() {
            return deleted;
        }

        public void reset() {
            changed.clear();
            deleted.clear();
            recordChangeCallCounter = 0;
            deleteCallCounter = 0;
        }

        public int getRecordChangeCallCounter() {
            return recordChangeCallCounter;
        }

        public int getDeleteCallCounter() {
            return deleteCallCounter;
        }
    }

    private TestCache testCache;

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

    private TrainingCache cache;
    private JunitListener listener;

    @Before
    public void before() {
        cache = TrainingCache.getInstance();
        cache.resetCache();
        listener = new JunitListener();
        testCache = new TestCache();
    }

    @After
    public void after() {
        listener.reset();
    }

    @Test
    public void testAddAll() {

        final ITraining a = Mockito.mock(ITraining.class);
        Mockito.when(a.getDatum()).thenReturn(42L);
        final ITraining b = Mockito.mock(ITraining.class);
        Mockito.when(b.getDatum()).thenReturn(43L);

        final List<ITraining> values = new ArrayList<>();
        values.add(a);
        values.add(b);

        cache.addAll(values);

        assertEquals("Zwei Elemente hinzugefügt", 2, cache.size());
        assertNotNull(cache.get(42L));
        assertNotNull(cache.get(43L));
    }

    @Test
    public void testRemoveSuccess() {

        cache.addListener(listener);

        final ITraining a = Mockito.mock(ITraining.class);
        Mockito.when(a.getDatum()).thenReturn(42L);
        final ITraining b = Mockito.mock(ITraining.class);
        Mockito.when(b.getDatum()).thenReturn(43L);

        final List<ITraining> values = new ArrayList<>();
        values.add(a);
        values.add(b);

        cache.addAll(values);

        assertEquals("Zwei Elemente hinzugefügt", 2, cache.size());

        cache.remove(42L);
        assertEquals("Zwei Elemente hinzugefügt", 1, cache.size());
        assertNull(cache.get(42L));
        assertNotNull(cache.get(43L));
        assertEquals("Zwei vom hinzufügen", 2, listener.getChanged().size());
        assertEquals(1, listener.getDeleted().size());
    }

    @Test
    public void testRemoveListSuccess() {

        cache.addListener(listener);

        final ITraining a = Mockito.mock(ITraining.class);
        Mockito.when(a.getDatum()).thenReturn(42L);
        final ITraining b = Mockito.mock(ITraining.class);
        Mockito.when(b.getDatum()).thenReturn(43L);

        final List<ITraining> values = new ArrayList<>();
        values.add(a);
        values.add(b);

        cache.addAll(values);

        assertEquals("Zwei Elemente hinzugefügt", 2, cache.size());
        final List<Long> keys = new ArrayList<>();
        keys.add(42L);
        keys.add(43L);
        cache.remove(keys);
        assertEquals("Zwei Elemente entfernt", 0, cache.size());
        assertNull(cache.get(42L));
        assertNull(cache.get(43L));
        assertEquals("Zwei vom hinzufügen", 2, listener.getChanged().size());
        assertEquals("Zwei vom löschen", 2, listener.getDeleted().size());
    }

    @Test
    public void testRemoveNotSuccess() {

        cache.addListener(listener);

        final ITraining a = Mockito.mock(ITraining.class);
        Mockito.when(a.getDatum()).thenReturn(42L);
        final ITraining b = Mockito.mock(ITraining.class);
        Mockito.when(b.getDatum()).thenReturn(43L);

        final List<ITraining> values = new ArrayList<>();
        values.add(a);
        values.add(b);

        cache.addAll(values);

        assertEquals("Zwei Elemente hinzugefügt", 2, cache.size());

        cache.remove(142L);
        assertEquals("Zwei Elemente hinzugefügt", 2, cache.size());
        assertNotNull(cache.get(42L));
        assertNotNull(cache.get(43L));
        assertEquals("Zwei vom hinzufügen", 2, listener.getChanged().size());
        assertEquals("kein element gelöscht", 0, listener.getDeleted().size());
    }

    @Test
    public void testContains() {
        final ITraining a = Mockito.mock(ITraining.class);
        Mockito.when(a.getDatum()).thenReturn(42L);
        final ITraining b = Mockito.mock(ITraining.class);
        Mockito.when(b.getDatum()).thenReturn(43L);

        final List<ITraining> values = new ArrayList<>();
        values.add(a);
        values.add(b);

        cache.addAll(values);

        assertTrue(cache.contains(42L));
        assertFalse(cache.contains(412L));
    }

    @Test
    public void testGetAll() {
        final ITraining a = Mockito.mock(ITraining.class);
        Mockito.when(a.getDatum()).thenReturn(42L);
        final ITraining b = Mockito.mock(ITraining.class);
        Mockito.when(b.getDatum()).thenReturn(43L);

        final List<ITraining> values = new ArrayList<>();
        values.add(a);
        values.add(b);

        cache.addAll(values);

        final List<ITraining> all = cache.getAll();
        assertEquals("Zeitlich sortiert 43 > 42", 42L, all.get(1).getDatum());
        assertEquals("Zeitlich sortiert 43 > 42", 43L, all.get(0).getDatum());
    }

    @Test
    public void testNotifyListeners() {
        final JunitListener a = new JunitListener();
        final JunitListener b = new JunitListener();

        cache.addListener(a);

        cache.notifyListeners();

        assertEquals(1, a.getRecordChangeCallCounter());
        assertEquals("Dieser Listener wurde nicht hinzugefügt", 0, b.getRecordChangeCallCounter());
    }

    @Test
    public void testRemoveListener() {
        final JunitListener a = new JunitListener();
        final JunitListener b = new JunitListener();

        cache.addListener(a);
        cache.addListener(b);

        cache.notifyListeners();

        assertEquals(1, a.getRecordChangeCallCounter());
        assertEquals(1, b.getRecordChangeCallCounter());

        cache.removeListener(b);

        cache.notifyListeners();

        assertEquals(2, a.getRecordChangeCallCounter());
        assertEquals(1, b.getRecordChangeCallCounter());
    }

    @Test
    public void testFireEventEvenNoElementInCacheWasDeleted() {
        final JunitListener a = new JunitListener();
        final JunitListener b = new JunitListener();

        cache.addListener(a);
        cache.addListener(b);

        cache.remove(42L);

        assertEquals(1, a.getDeleteCallCounter());
        assertEquals(1, b.getDeleteCallCounter());
    }

    // some old tests

    @Test
    public void testSimpleAdd() {
        final CacheElement value = new CacheElement(42, "FirstElement");
        testCache.add(value);
        assertEquals("Element muss im cache gefunden werden", value, testCache.get(42));
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
        testCache.addAll(list);
        for (final CacheElement cacheElement : list) {
            assertEquals("Element muss im cache gefunden werden", cacheElement, testCache.get(cacheElement.getK()));
            assertTrue("Element muss im cache gefunden werden", testCache.contains(cacheElement.getK()));
        }
    }

    @Test
    public void testSimpleRemove() {
        final CacheElement value = new CacheElement(42, "FirstElement");
        testCache.add(value);
        assertEquals("Element muss im cache gefunden werden", value, testCache.get(42));
        testCache.remove(42);
        assertNull("Element nicht mehr im cache", testCache.get(42));
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
        testCache.addAll(list);
        assertEquals("3 Elemente sind nun im cache", 3, testCache.size());
        testCache.remove(Arrays.asList(new Integer[] { 42, 43, 44 }));
        for (final CacheElement cacheElement : list) {
            assertNull("Element nicht mehr im cache", testCache.get(cacheElement.getK()));
        }
        assertEquals("Cache ist leer", 0, testCache.size());
    }

    @Test
    public void testAddListener() {
        final List<CacheElement> changed = new ArrayList<AbstractCacheTest.CacheElement>();
        final List<CacheElement> deleted = new ArrayList<AbstractCacheTest.CacheElement>();
        testCache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

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
        testCache.add(value1);
        assertEquals("Listener wurde notifiziert", value1, changed.get(0));
        testCache.remove(42);
        assertEquals("Listener wurde notifiziert", value1, deleted.get(0));
    }

    @Test
    public void testAddListeners() {
        final List<CacheElement> changed = new ArrayList<AbstractCacheTest.CacheElement>();
        final List<CacheElement> deleted = new ArrayList<AbstractCacheTest.CacheElement>();
        testCache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

            @Override
            public void recordChanged(final Collection<CacheElement> entry) {
                changed.addAll(entry);
            }

            @Override
            public void deleteRecord(final Collection<CacheElement> entry) {
                deleted.addAll(entry);
            }
        });

        testCache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

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
        testCache.add(value1);
        assertEquals("2 Listener wurde notifiziert", 2, changed.size());
        testCache.remove(42);
        assertEquals("2 Listener wurde notifiziert", 2, deleted.size());
    }

    @Test
    public void testNotifyListeners2() {
        final Object[] value = new Object[1];
        testCache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

            @Override
            public void recordChanged(final Collection<CacheElement> entry) {
                value[0] = entry;
            }

            @Override
            public void deleteRecord(final Collection<CacheElement> entry) {
                // do nothing
            }
        });

        testCache.notifyListeners();
        assertNull("Listener wurde mit null notifiziert", value[0]);
    }

}
