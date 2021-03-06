package ch.opentrainingcenter.core.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class AbstractCacheTest {

    class JunitListener implements IRecordListener<ITraining> {

        private final Collection<ITraining> added = new ArrayList<>();
        private final Collection<ITraining> changed = new ArrayList<>();
        private final Collection<ITraining> deleted = new ArrayList<>();
        private int recordAddedCallCounter = 0;
        private int recordChangeCallCounter = 0;
        private int deleteCallCounter = 0;

        @Override
        public void onEvent(final Collection<ITraining> entry, final Event event) {
            switch (event) {
            case ADDED:
                recordAdded(entry);
                break;
            case CHANGED:
                recordChanged(entry);
                break;
            case DELETED:
                deleteRecord(entry);
                break;
            }
        }

        private void recordChanged(final Collection<ITraining> entry) {
            if (entry != null) {
                changed.addAll(entry);
            }
            recordChangeCallCounter++;
        }

        private void deleteRecord(final Collection<ITraining> entry) {
            deleted.addAll(entry);
            deleteCallCounter++;
        }

        private void recordAdded(final Collection<ITraining> entry) {
            added.addAll(entry);
            recordAddedCallCounter++;
        }

        public Collection<ITraining> getChanged() {
            return changed;
        }

        public Collection<ITraining> getDeleted() {
            return deleted;
        }

        public Collection<ITraining> getAdded() {
            return added;
        }

        public void reset() {
            changed.clear();
            deleted.clear();
            added.clear();
            recordChangeCallCounter = 0;
            deleteCallCounter = 0;
            recordAddedCallCounter = 0;
        }

        public int getRecordChangeCallCounter() {
            return recordChangeCallCounter;
        }

        public int getDeleteCallCounter() {
            return deleteCallCounter;
        }

        public int getAddCallCounter() {
            return recordAddedCallCounter;
        }

    }

    private TestCache testCache;

    public class TestCache extends AbstractCache<Integer, CacheElement> {

        @Override
        public Integer getKey(final CacheElement value) {
            return value.getK();
        }

        @Override
        public String getName() {
            return "Junit";
        }
    }

    public class CacheElement {
        private final int k;
        private String v;

        public CacheElement(final int k, final String v) {
            this.k = k;
            this.v = v;
        }

        public int getK() {
            return k;
        }

        public String getV() {
            return v;
        }

        public void setV(final String v) {
            this.v = v;
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

        assertEquals("Kein Update", 0, listener.getChanged().size());
        assertEquals("Zwei vom hinzufügen", 2, listener.getAdded().size());
        assertEquals(1, listener.getDeleted().size());
    }

    @Test
    public void testRemoveListSuccess() {

        cache.addListener(listener);

        final ITraining a = Mockito.mock(ITraining.class);
        Mockito.when(a.getDatum()).thenReturn(42L);
        final ITraining b = Mockito.mock(ITraining.class);
        Mockito.when(b.getDatum()).thenReturn(43L);
        final ITraining c = Mockito.mock(ITraining.class);
        Mockito.when(c.getDatum()).thenReturn(43L);

        final List<ITraining> values = new ArrayList<>();
        values.add(a);
        values.add(b);
        values.add(c);

        cache.addAll(values);

        assertEquals("Zwei Elemente hinzugefügt", 2, cache.size());
        final List<Long> keys = new ArrayList<>();
        keys.add(42L);
        keys.add(43L);
        cache.remove(keys);
        assertEquals("Zwei Elemente entfernt", 0, cache.size());
        assertNull(cache.get(42L));
        assertNull(cache.get(43L));
        assertEquals("Zwei vom hinzufügen", 2, listener.getAdded().size());
        assertEquals("Einer geändert", 1, listener.getChanged().size());
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
        assertEquals("Zwei vom hinzufügen", 2, listener.getAdded().size());
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
    public void testDoNotNotifyListeners() {
        final JunitListener a = new JunitListener();
        final JunitListener b = new JunitListener();

        cache.addListener(a);
        cache.addListener(b);

        cache.addAll(new ArrayList<ITraining>());

        assertEquals(0, a.getRecordChangeCallCounter());
        assertEquals(0, b.getRecordChangeCallCounter());
    }

    @Test
    public void testDoNotifyListeners() {
        final ITraining trainingA = Mockito.mock(ITraining.class);
        Mockito.when(trainingA.getDatum()).thenReturn(42L);

        final JunitListener a = new JunitListener();
        final JunitListener b = new JunitListener();

        cache.addListener(a);
        cache.addListener(b);

        final ArrayList<ITraining> values = new ArrayList<ITraining>();
        values.add(trainingA);
        cache.addAll(values);

        assertEquals(1, a.getAddCallCounter());
        assertEquals(1, b.getAddCallCounter());
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

        final List<CacheElement> models = new ArrayList<>();
        models.add(value);

        testCache.addAll(models);

        assertEquals("Element muss im cache gefunden werden", value, testCache.get(42));
    }

    @Test
    public void testSimpleUpdate() {
        final CacheElement value = new CacheElement(42, "FirstElement");

        final List<CacheElement> models = new ArrayList<>();
        models.add(value);

        testCache.addAll(models);

        assertEquals("Element muss im cache gefunden werden", value, testCache.get(42));

        models.clear();
        final String newValue = "blabla";
        value.setV(newValue);
        models.add(value);

        testCache.addAll(models);

        assertEquals("Updated wert muss vorhanden sein", newValue, testCache.get(42).getV());
        assertEquals(1, testCache.size());
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

    @Test(expected = IllegalArgumentException.class)
    public void testAddNull() {
        testCache.addAll(null);
    }

    @Test
    public void testSimpleRemove() {
        final CacheElement value = new CacheElement(42, "FirstElement");

        final List<CacheElement> models = new ArrayList<>();
        models.add(value);

        testCache.addAll(models);

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
        final List<CacheElement> added = new ArrayList<AbstractCacheTest.CacheElement>();
        final List<CacheElement> changed = new ArrayList<AbstractCacheTest.CacheElement>();
        final List<CacheElement> deleted = new ArrayList<AbstractCacheTest.CacheElement>();
        testCache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

            @Override
            public void onEvent(final Collection<CacheElement> entry, final Event event) {
                switch (event) {
                case ADDED:
                    added.addAll(entry);
                    break;
                case CHANGED:
                    changed.addAll(entry);
                    break;
                case DELETED:
                    deleted.addAll(entry);
                    break;
                }
            }

        });
        final CacheElement value1 = new CacheElement(42, "ValueA");
        final CacheElement value2 = new CacheElement(42, "ValueB");
        final CacheElement value3 = new CacheElement(43, "ValueC");

        final List<CacheElement> models = new ArrayList<>();
        models.add(value1);
        models.add(value2);
        models.add(value3);

        testCache.addAll(models);

        assertEquals("Listener wurde notifiziert", value1, added.get(0));
        assertEquals("Listener wurde notifiziert", value3, added.get(1));

        assertEquals("Listener wurde notifiziert", value2, changed.get(0));
        testCache.remove(42);
        assertEquals("Listener wurde notifiziert", value2, deleted.get(0));
    }

    @Test
    public void testAddListeners() {
        final List<CacheElement> added = new ArrayList<AbstractCacheTest.CacheElement>();
        final List<CacheElement> changed = new ArrayList<AbstractCacheTest.CacheElement>();
        final List<CacheElement> deleted = new ArrayList<AbstractCacheTest.CacheElement>();
        testCache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

            @Override
            public void onEvent(final Collection<CacheElement> entry, final Event event) {
                switch (event) {
                case ADDED:
                    added.addAll(entry);
                    break;
                case CHANGED:
                    changed.addAll(entry);
                    break;
                case DELETED:
                    deleted.addAll(entry);
                    break;
                }
            }
        });

        testCache.addListener(new IRecordListener<AbstractCacheTest.CacheElement>() {

            @Override
            public void onEvent(final Collection<CacheElement> entry, final Event event) {
                switch (event) {
                case ADDED:
                    added.addAll(entry);
                    break;
                case CHANGED:
                    changed.addAll(entry);
                    break;
                case DELETED:
                    deleted.addAll(entry);
                    break;
                }
            }
        });
        final CacheElement value1 = new CacheElement(42, "1Element");
        final List<CacheElement> models = new ArrayList<>();
        models.add(value1);

        testCache.addAll(models);

        assertEquals("2 Listener wurde notifiziert", 2, added.size());
        testCache.remove(42);
        assertEquals("2 Listener wurde notifiziert", 2, deleted.size());
    }
}
