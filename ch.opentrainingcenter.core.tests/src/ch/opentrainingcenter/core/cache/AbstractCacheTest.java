package ch.opentrainingcenter.core.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.transfer.ITraining;

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

    private TrainingCache cache;
    private JunitListener listener;

    @Before
    public void before() {
        cache = (TrainingCache) TrainingCache.getInstance();
        cache.resetCache();
        listener = new JunitListener();
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
        assertEquals("", 42L, all.get(0).getDatum());
        assertEquals("", 43L, all.get(1).getDatum());
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
}
