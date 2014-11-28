package ch.opentrainingcenter.core.cache.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.cache.ActivityTTestHelper;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.MockRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.TrainingType;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class TrainingCacheTest {

    private static IDatabaseAccess mockDataAccess;
    private static Cache cache;

    final MockRecordListener<ITraining> listener = new MockRecordListener<ITraining>();

    @Before
    public void before() {
        mockDataAccess = Mockito.mock(IDatabaseAccess.class);
        cache = TrainingCache.getInstance();
        cache.resetCache();
        // ApplicationContext.getApplicationContext().clear();
    }

    @After
    public void tearDown() {
        cache.resetCache();
    }

    @Test
    public void testNotNull() {
        assertNotNull(cache);
    }

    @Test
    public void testGetIfEmpty() {
        final ITraining training = cache.get(42L);
        assertNull("Wenn nichts hinzugefügt wurde, kommt auch nix zurück.", training);
    }

    @Test
    public void simpleAdd() throws DatatypeConfigurationException {
        final ITraining training = ActivityTTestHelper.createActivity(2012);

        final List<ITraining> models = new ArrayList<>();
        models.add(training);
        // execute
        cache.addAll(models);

        // assert
        final ITraining trainingFromCache = cache.get(training.getDatum());
        assertNotNull("Activity muss im Cache gefunden werden: ", trainingFromCache);
    }

    @Test
    public void testRemove() throws DatatypeConfigurationException {
        // prepare

        cache.addListener(listener);

        final ITraining trainingA = ActivityTTestHelper.createActivity(2012);
        final ITraining trainingB = ActivityTTestHelper.createActivity(2013);

        final Long dateA = trainingA.getDatum();
        final Long dateB = trainingB.getDatum();

        final List<ITraining> models = new ArrayList<>();
        models.add(trainingA);
        models.add(trainingB);

        cache.addAll(models);

        final List<Long> deletedIds = new ArrayList<Long>();
        deletedIds.add(dateA);
        deletedIds.add(dateB);
        // execute
        cache.remove(deletedIds);

        // assert
        assertEquals("Beide Record-Deleted müssen an Listener propagiert werden", 2, listener.getDeletedEntry().size());
    }

    @Test
    public void testRemoveOne() throws DatatypeConfigurationException {
        // prepare
        cache.addListener(listener);
        final ITraining trainingA = ActivityTTestHelper.createActivity(2012);
        final ITraining trainingB = ActivityTTestHelper.createActivity(2013);

        final Long dateA = trainingA.getDatum();
        final Long dateB = trainingB.getDatum();
        final ITraining impA = createImported(dateA);
        final ITraining impB = createImported(dateB);

        Mockito.when(mockDataAccess.getTrainingById(dateA)).thenReturn(impA);
        Mockito.when(mockDataAccess.getTrainingById(dateB)).thenReturn(impB);

        final List<ITraining> models = new ArrayList<>();
        models.add(trainingA);
        models.add(trainingB);

        cache.addAll(models);

        final List<Long> deletedIds = new ArrayList<Long>();
        deletedIds.add(42L);
        deletedIds.add(dateB);
        // execute
        cache.remove(deletedIds);

        // assert
        assertEquals("Ein Record-Deleted müssen an Listener propagiert werden", 1, listener.getDeletedEntry().size());
    }

    @Test
    public void testUpdate() {
        // prepare
        cache.addListener(listener);
        // execute
        cache.addAll(new ArrayList<ITraining>());
        // assert
        assertEquals("Nichts wurde upgedated", 0, listener.getChangedEntry().size());
        assertEquals("Nichts wurde upgedated", 0, listener.getDeletedEntry().size());
    }

    @Test
    public void removeListenerTestMitNull() {
        cache.removeListener(null);
    }

    @Test
    public void removeListenerTest() {
        // prepare
        cache.removeListener(listener);
    }

    @Test
    public void containsMitNull() {
        assertFalse("Nichts im cache", cache.contains(-42L));
    }

    @Test
    public void containsTest() throws DatatypeConfigurationException {

        // prepare
        final ITraining trainingA = ActivityTTestHelper.createActivity(2012);
        final ITraining trainingB = ActivityTTestHelper.createActivity(2013);

        final Long dateA = trainingA.getDatum();
        final Long dateB = trainingB.getDatum();

        final List<ITraining> models = new ArrayList<>();
        models.add(trainingA);
        models.add(trainingB);

        cache.addAll(models);

        assertTrue("Erster Record ist im Cache (" + dateA + ")", cache.contains(dateA));
        assertTrue("Zweiter Record ist im Cache (" + dateB + ")", cache.contains(dateB));
        assertFalse("Wenn nichts gefunden", cache.contains(-42L));
    }

    @Test
    public void testNotesNull() throws DatatypeConfigurationException {
        // prepare
        final ITraining trainingA = ActivityTTestHelper.createActivity(2012);

        final Long dateA = trainingA.getDatum();

        // execute
        cache.update(dateA, "", null, null);

        final List<ITraining> models = new ArrayList<>();
        models.add(trainingA);

        cache.addAll(models);

        // assert
        final ITraining trainingTFromCache = cache.get(dateA);
        assertEquals("Note ist nicht gesetzt, da Record erst später in cache kam", "", trainingTFromCache.getNote());
    }

    @Test
    public void testNotes() throws DatatypeConfigurationException {
        // prepare
        final ITraining trainingA = ActivityTTestHelper.createActivity(2012);

        final Long dateA = trainingA.getDatum();

        final List<ITraining> models = new ArrayList<>();
        models.add(trainingA);

        cache.addAll(models);

        // execute
        cache.update(dateA, "test", null, null);

        // assert
        final ITraining trainingTFromCache = cache.get(dateA);

        assertEquals("Note muss korrekt gesetzt sein", "test", trainingTFromCache.getNote());
    }

    @Test
    public void testNotesNotification() throws DatatypeConfigurationException {
        // prepare
        cache.addListener(listener);
        final ITraining trainingA = ActivityTTestHelper.createActivity(2012);

        final Long dateA = trainingA.getDatum();

        final List<ITraining> models = new ArrayList<>();
        models.add(trainingA);

        cache.addAll(models);

        // execute
        cache.update(dateA, "test", null, null);

        // assert
        final ITraining trainingTFromCache = cache.get(dateA);

        assertEquals("Note muss korrekt gesetzt sein", "test", trainingTFromCache.getNote());
        assertEquals("Ein Record-Changed muss an Listener propagiert werden", 1, listener.getChangedEntry().size());
    }

    @Test
    public void testUpdateOhneListener() {
        cache.addAll(new ArrayList<ITraining>());

        assertEquals("Eh kein listener angehängt", 0, listener.getChangedEntry().size());
        assertEquals("Eh kein listener angehängt", 0, listener.getDeletedEntry().size());
    }

    @Test
    public void testToStringInit() {
        assertEquals("Training-Cache: Anzahl Elemente: 0", cache.toString());
    }

    @Test
    public void testToString() throws DatatypeConfigurationException {
        // prepare
        final ITraining trainingA = ActivityTTestHelper.createActivity(2012);
        final ITraining trainingB = ActivityTTestHelper.createActivity(2013);

        final List<ITraining> models = new ArrayList<>();
        models.add(trainingA);
        models.add(trainingB);

        cache.addAll(models);

        assertEquals("Training-Cache: Anzahl Elemente: 2", cache.toString());
    }

    private ITraining createImported(final Long date) {
        final ITraining overview = CommonTransferFactory.createTraining(new RunData(date, 1, 1, 1), new HeartRate(1, 1));
        final TrainingType type = TrainingType.NONE;
        overview.setTrainingType(type);
        return overview;
    }

}
